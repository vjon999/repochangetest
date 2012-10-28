package filesystem;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class AppendableOutputStream extends ObjectOutputStream {
	public AppendableOutputStream(OutputStream outputStream) throws IOException {
		super(outputStream);
	}
	
	protected void writeStreamHeader() throws IOException {
		// donot write the stream header
    }
}
