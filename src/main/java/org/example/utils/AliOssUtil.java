package org.example.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Aliyun OSS file operation utility.
 */
@Component
public class AliOssUtil {

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.access-key-id}")
    private String accessKeyId;

    @Value("${aliyun.oss.access-key-secret}")
    private String accessKeySecret;

    @Value("${aliyun.oss.bucket-name}")
    private String bucketName;

    @Value("${aliyun.oss.url-prefix:}")
    private String urlPrefix;

    /**
     * Upload a file stream to OSS and return its public access URL.
     */
    public String uploadFile(String objectName, InputStream in) {
        return uploadFile(objectName, in, null);
    }

    /**
     * Upload a file stream to OSS with content type and return its public access URL.
     */
    public String uploadFile(String objectName, InputStream in, String contentType) {
        if (!StringUtils.hasText(objectName)) {
            throw new IllegalArgumentException("objectName must not be blank");
        }
        if (in == null) {
            throw new IllegalArgumentException("input stream must not be null");
        }

        OSS ossClient = createOssClient();
        try {
            String normalizedObjectName = normalizeObjectName(objectName);
            PutObjectRequest request = new PutObjectRequest(bucketName, normalizedObjectName, in);
            if (StringUtils.hasText(contentType)) {
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(contentType);
                request.setMetadata(metadata);
            }

            ossClient.putObject(request);
            return getFileUrl(normalizedObjectName);
        } catch (OSSException e) {
            throw new RuntimeException("OSS upload failed: " + e.getErrorMessage(), e);
        } catch (ClientException e) {
            throw new RuntimeException("OSS client error: " + e.getMessage(), e);
        } finally {
            ossClient.shutdown();
        }
    }

    /**
     * Upload a MultipartFile to a directory in OSS and return its public access URL.
     */
    public String uploadFile(MultipartFile file, String directory) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("upload file must not be empty");
        }

        String objectName = buildObjectName(file.getOriginalFilename(), directory);
        try (InputStream in = file.getInputStream()) {
            return uploadFile(objectName, in, file.getContentType());
        } catch (IOException e) {
            throw new RuntimeException("read upload file failed: " + e.getMessage(), e);
        }
    }

    /**
     * Delete a file from OSS.
     */
    public void deleteFile(String objectName) {
        if (!StringUtils.hasText(objectName)) {
            throw new IllegalArgumentException("objectName must not be blank");
        }

        OSS ossClient = createOssClient();
        try {
            ossClient.deleteObject(bucketName, normalizeObjectName(objectName));
        } catch (OSSException e) {
            throw new RuntimeException("OSS delete failed: " + e.getErrorMessage(), e);
        } catch (ClientException e) {
            throw new RuntimeException("OSS client error: " + e.getMessage(), e);
        } finally {
            ossClient.shutdown();
        }
    }

    /**
     * Build a public URL for the object.
     */
    public String getFileUrl(String objectName) {
        String normalizedName = normalizeObjectName(objectName);
        if (StringUtils.hasText(urlPrefix)) {
            return trimEnd(urlPrefix, "/") + "/" + encodeObjectName(normalizedName);
        }
        return "https://" + bucketName + "." + trimEndpoint(endpoint) + "/" + encodeObjectName(normalizedName);
    }

    private OSS createOssClient() {
        validateConfig();
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }

    private void validateConfig() {
        if (!StringUtils.hasText(endpoint)) {
            throw new IllegalStateException("aliyun.oss.endpoint is not configured");
        }
        if (!StringUtils.hasText(accessKeyId)) {
            throw new IllegalStateException("aliyun.oss.access-key-id is not configured");
        }
        if (!StringUtils.hasText(accessKeySecret)) {
            throw new IllegalStateException("aliyun.oss.access-key-secret is not configured");
        }
        if (!StringUtils.hasText(bucketName)) {
            throw new IllegalStateException("aliyun.oss.bucket-name is not configured");
        }
    }

    private String buildObjectName(String originalFilename, String directory) {
        String extension = StringUtils.getFilenameExtension(originalFilename);
        String filename = UUID.randomUUID().toString().replace("-", "");
        if (StringUtils.hasText(extension)) {
            filename = filename + "." + extension;
        }

        String datePath = LocalDate.now().toString().replace("-", "/");
        String prefix = StringUtils.hasText(directory) ? trimEnd(directory, "/") + "/" : "";
        return normalizeObjectName(prefix + datePath + "/" + filename);
    }

    private String normalizeObjectName(String objectName) {
        return trimStart(objectName, "/");
    }

    private String encodeObjectName(String objectName) {
        String[] parts = objectName.split("/");
        StringBuilder encodedName = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) {
                encodedName.append("/");
            }
            encodedName.append(URLEncoder.encode(parts[i], StandardCharsets.UTF_8).replace("+", "%20"));
        }
        return encodedName.toString();
    }

    private String trimEndpoint(String endpoint) {
        return trimStart(endpoint, "https://", "http://");
    }

    private String trimStart(String value, String... prefixes) {
        String result = value == null ? "" : value;
        boolean changed;
        do {
            changed = false;
            for (String prefix : prefixes) {
                if (result.startsWith(prefix)) {
                    result = result.substring(prefix.length());
                    changed = true;
                }
            }
        } while (changed);
        return result;
    }

    private String trimEnd(String value, String suffix) {
        String result = value == null ? "" : value;
        while (result.endsWith(suffix)) {
            result = result.substring(0, result.length() - suffix.length());
        }
        return result;
    }
}
