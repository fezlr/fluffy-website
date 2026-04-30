package fezlr.fluffy.photo.service;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class PhotoStorageService {
    private final Cloudinary cloudinary;

    public String uploadPhoto(MultipartFile file) {
        try {
            Map<?, ?> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    Map.of(
                            "folder", "profiles",
                            "resource_type", "image"
                    )
            );

            return result.get("secure_url").toString();
        } catch(IOException e) {
            throw new RuntimeException("Photo upload failed", e);
        }
    }
}