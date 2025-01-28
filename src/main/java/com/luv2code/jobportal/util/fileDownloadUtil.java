package com.luv2code.jobportal.util;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/*


Usage Example
Consider a directory structure as follows:

markdown
Copy code
photos/candidate/user123/
  - file1.txt
  - file2.jpg
  - file3.pdf
If you call Files.list(path) where path points to photos/candidate/user123, the method will
return a stream that contains paths to file1.txt, file2.jpg, and file3.pdf.

(.)Directory Entries: The stream will include only the entries (files and directories) that are directly
within the specified directory, not its subdirectories.
 */
public class fileDownloadUtil {
    private Path foundFile;
    public Resource getFileResource(String downloadDir, String fileName) throws IOException {
        Path path= Paths.get(downloadDir);
        Files.list(path).forEach(file->{
            if(file.getFileName().toString().startsWith(fileName)){
                foundFile=file;
            }
        });
        if(foundFile!=null){
          return new UrlResource(foundFile.toUri());
        }
        return null;
    }
}
