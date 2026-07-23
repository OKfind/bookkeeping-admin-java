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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Base64;
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
     * Upload a Base64 image string to a directory in OSS and return its public access URL.
     */
    public String uploadFile(String imageData, String directory) {
        if (!StringUtils.hasText(imageData)) {
            throw new IllegalArgumentException("image data must not be blank");
        }

        String trimmedImageData = imageData.trim();
        if (trimmedImageData.startsWith("http://") || trimmedImageData.startsWith("https://")) {
            return trimmedImageData;
        }

        ImageContent imageContent = decodeImageData(trimmedImageData);
        String objectName = buildImageObjectName(imageContent.extension, directory);
        return uploadFile(objectName, new ByteArrayInputStream(imageContent.bytes), imageContent.contentType);
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

    private String buildImageObjectName(String extension, String directory) {
        String filename = UUID.randomUUID().toString().replace("-", "") + "." + extension;
        if (!StringUtils.hasText(directory)) {
            return filename;
        }
        return normalizeObjectName(trimEnd(directory, "/") + "/" + filename);
    }

    private ImageContent decodeImageData(String imageData) {
        String contentType = null;
        String base64Data = imageData;

        if (imageData.startsWith("data:")) {
            int commaIndex = imageData.indexOf(",");
            if (commaIndex < 0 || !imageData.substring(0, commaIndex).contains(";base64")) {
                throw new IllegalArgumentException("bill image must be a Base64 image");
            }

            String mediaType = imageData.substring("data:".length(), commaIndex);
            int semicolonIndex = mediaType.indexOf(";");
            contentType = semicolonIndex >= 0 ? mediaType.substring(0, semicolonIndex) : mediaType;
            base64Data = imageData.substring(commaIndex + 1);
        }

        try {
            byte[] bytes = Base64.getDecoder().decode(base64Data.replaceAll("\\s", ""));
            String extension = getImageExtension(contentType, bytes);
            String resolvedContentType = StringUtils.hasText(contentType) ? contentType : "image/" + extension;
            return new ImageContent(bytes, resolvedContentType, extension);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("bill image must be a Base64 image", e);
        }
    }

    private String getImageExtension(String contentType, byte[] bytes) {
        if ("image/png".equals(contentType)) {
            return "png";
        }
        if ("image/gif".equals(contentType)) {
            return "gif";
        }
        if ("image/webp".equals(contentType)) {
            return "webp";
        }
        if ("image/jpeg".equals(contentType) || "image/jpg".equals(contentType)) {
            return "jpg";
        }
        if (bytes.length >= 8
                && bytes[0] == (byte) 0x89
                && bytes[1] == 0x50
                && bytes[2] == 0x4E
                && bytes[3] == 0x47) {
            return "png";
        }
        if (bytes.length >= 3
                && bytes[0] == (byte) 0xFF
                && bytes[1] == (byte) 0xD8
                && bytes[2] == (byte) 0xFF) {
            return "jpg";
        }
        return "jpg";
    }

    private static class ImageContent {
        private final byte[] bytes;
        private final String contentType;
        private final String extension;

        private ImageContent(byte[] bytes, String contentType, String extension) {
            this.bytes = bytes;
            this.contentType = contentType;
            this.extension = extension;
        }
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
