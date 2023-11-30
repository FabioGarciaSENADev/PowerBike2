package com.PowerBike.utils.moreUtils;

import com.PowerBike.repository.ProductRepository;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class UploadFileService implements DisposableBean {

    //@Value("$path.image-products")
    private final String folder = "src/main/resources/static/productImages/";

    @Autowired
    private ProductRepository productRepository;

    //Metodo para eliminar imagenes que no pertenecen a un producto, se ejecuta en el contexto de cierre de app
    @Override
    public void destroy() throws Exception {
        System.out.println("Realizando limpieza de imágenes al cerrar la aplicación.");
        CleanUnreferencedImage();
    }

    public String saveImage(MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            String originalFileName = file.getOriginalFilename();
            String uniqueFileName = UUID.randomUUID().toString();
            String fileExtension = StringUtils.getFilenameExtension(originalFileName);
            String newFileName = uniqueFileName + "." + fileExtension;
            byte[] fileBytes = file.getBytes();
            Path path = Paths.get(folder + newFileName);

            //Si la carpeta no esta creada la crea
            createDirFolder();

            //Guardo la imagen si no existe
            if (!Files.exists(path)) {
                try {
                    Files.write(path, fileBytes);
                    System.out.println("Imagen guardada exitosamente");
                    return newFileName;
                } catch (Exception e) {
                    System.err.println("Error al guardar la imagen: " + e.getMessage());
                }
            }
        }
        return "default.jpg";
    }

    public void deleteImage(String fileName) {
        File file = new File(folder + fileName);
        file.delete();
    }

    public void createDirFolder() {
        File dirFolder = new File(folder);
        if (!dirFolder.exists()) {
            dirFolder.mkdirs();
        }
    }

    private void CleanUnreferencedImage() {
        File dirFolder = new File(folder);
        if (dirFolder.exists() && dirFolder.isDirectory()) {
            File[] ImagesInFolder = dirFolder.listFiles();
            if (ImagesInFolder != null) {
                for (File image : ImagesInFolder) {
                    String nameImage = image.getName();
                    if (!productRepository.existsByImage(nameImage) && !nameImage.equals("default.jpg")) {
                        image.delete();
                        System.out.println("Imagen eliminada: " + nameImage);
                    }
                }
            }
        }
    }

}
