package gui;

import com.sun.org.apache.xml.internal.serialize.LineSeparator;

import engines.Engine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Pratik
 */
public class EngineConfigs {
    
    private Set<Engine> uciEngines = new HashSet<Engine>();

    public void saveConfig(OutputStream os) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
        for(Engine uciStructure : uciEngines) {
            if(null != uciStructure)
                if(uciStructure.toString().length() > 10)
                    writer.write(uciStructure.toString() + LineSeparator.Windows);
        }
        writer.close();
    }
    
    public void readConfig(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line = null;
        boolean isNew = true;
        while(null != (line = reader.readLine())) {
            System.out.println(line);
            Engine newEngine = new Engine(line);  
            for(Engine engine : uciEngines) {
                if(newEngine.getEngineId().equalsIgnoreCase(engine.getEngineId())) {
                    isNew = false;
                    break;
                }
            }
            if(isNew) {
                System.out.println(newEngine);
                uciEngines.add(newEngine);
            }
        }
        reader.close();
    }

	public Set<Engine> getUciEngines() {
		return uciEngines;
	}

	public void setUciEngines(Set<Engine> uciEngines) {
		this.uciEngines = uciEngines;
	}
}

