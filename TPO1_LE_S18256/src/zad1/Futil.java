package zad1;



import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;
import java.io.IOException;

public class Futil {

	static Charset charsetOut = Charset.forName("UTF-8");
	static Charset charsetIn = Charset.forName("Cp1250");
	static CharSequence cs =null;
	static EnumSet<StandardOpenOption> outOpts=EnumSet.of(CREATE, WRITE);
	
	public static void processDir(String dirName, String resultFileName)  {
		
		
			try {
				Files.deleteIfExists(Paths.get(resultFileName));
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
			//FileChannel fcIn;
			//FileChannel fcOut;
			
		
			try ( FileChannel fcout = FileChannel.open(Paths.get(resultFileName),EnumSet.of(CREATE, WRITE) )) {  
				Files.walkFileTree(Paths.get(dirName), new SimpleFileVisitor<Object>() { 
				
					@Override
					public FileVisitResult visitFile(Object file, BasicFileAttributes attrs) throws IOException {	
					
						
						try (FileChannel fc = FileChannel.open((Path)file)) {   
							cs = charsetIn.decode(read(fc, newBuffer(fc)));
							fcout.write(charsetOut.encode(CharBuffer.wrap(cs + "\r\n")));  
						} 
					
						return FileVisitResult.CONTINUE;
					}
				});
			} 
		 catch(IOException ex) {
			ex.printStackTrace();
			
			return;}
		} 
	
	
    private static ByteBuffer newBuffer(FileChannel fc) throws IOException { 
    	return ByteBuffer.allocateDirect((int)fc.size()); 
    }
    
    private static ByteBuffer read(FileChannel fc, ByteBuffer bybu) throws IOException { 
    	fc.read(bybu); 
    	bybu.flip(); 
    	
    	return bybu; 
    }
}			
		