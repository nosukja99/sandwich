package com.example.sandwitch;

import com.cloudinary.Cloudinary;
import com.cloudinary.Singleton;
import com.cloudinary.Transformation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class CloudinaryConfig {

    private Cloudinary cloudinary;

    public CloudinaryConfig(@Value("${cloudinary.apikey}") String key,@Value("${cloudinary.secret}") String secret,
    @Value("${cloudinary.name}") String name)
    {
        cloudinary= Singleton.getCloudinary();
        cloudinary.config.cloudName=name;
        cloudinary.config.apiKey=key;
        cloudinary.config.apiSecret=secret;
    }

    public Map upload(Object file, Map options)
    {
        try
        {
            return cloudinary.uploader().upload(file, options);
        }catch(IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public String CreateUrl(String name, int width, int height, String action)
    {
        return cloudinary.url().transformation
                (new Transformation().width(width).height(height).border("2px_solid.black").crop(action)).imageTag(name);
    }
}
