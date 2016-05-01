package com.prapps.rail.helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prapps.rail.KVPair;
import com.prapps.rail.Station;
import com.prapps.rail.Stop;
import com.prapps.rail.Train;

public class FileOp {

	public void save(String data, String file) throws IOException {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
			writer.write(data);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw e;
			}
		}

	}

	public String readFile(String file) throws IOException {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw e;
			}
		}
	}

	public Collection<Train> loadTrains() {
		BufferedReader reader = null;
		Collection<Train> trains = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream("trains.json")));
			StringBuilder sb = new StringBuilder();
			String line = null;
			Gson gson = new Gson();
			Type listType = new TypeToken<Collection<Train>>() {
			}.getType();
			trains = gson.fromJson(reader, listType);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return trains;
	}
	
	public Collection<KVPair> loadAllStations() {
		BufferedReader reader = null;
		Collection<KVPair> stations = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream("data/StationCodes.json")));
			StringBuilder sb = new StringBuilder();
			String line = null;
			Gson gson = new Gson();
			Type listType = new TypeToken<Collection<KVPair>>() {
			}.getType();
			stations = gson.fromJson(reader, listType);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return stations;
	}
	
	public Collection<Stop> loadAllStops() {
		BufferedReader reader = null;
		Collection<Stop> stop = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream("data/train_stop.json")));
			StringBuilder sb = new StringBuilder();
			String line = null;
			Gson gson = new Gson();
			Type listType = new TypeToken<Collection<Stop>>() {
			}.getType();
			stop = gson.fromJson(reader, listType);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return stop;
	}
	
	public Collection<Station> loadJson(String fName) {
		BufferedReader reader = null;
		Collection<Station> stop = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(fName)));
			Gson gson = new Gson();
			Type listType = new TypeToken<Collection<Station>>() {}.getType();
			stop = gson.fromJson(reader, listType);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return stop;
	}

	public String getRoute(Train train) throws IOException {
		URL url = new URL("http://erail.in/" + train.getId() + "-" + train.getFrom().toLowerCase() + "-" + train.getTo().toLowerCase() + "-" + train.getType().toLowerCase() + "/route");
		System.out.println(url);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuilder sb = new StringBuilder();
		while ((inputLine = in.readLine()) != null)
			sb.append(inputLine);
		in.close();
		
		return sb.toString();
	}
	
	public Collection<Stop> parseHTML(String html) {
		Document doc;
		Collection<Stop> stops = new ArrayList<Stop>();
		// need http protocol
		doc = Jsoup.parse(html);
 
		// get page title
		String title = doc.title();
		//System.out.println("title : " + title);
 
		// get all links
		Elements links = doc.select("table");
		for (Element link : links) {
			if(link.className().startsWith("RouteList table table-bordered table-condensed")) {
				// get the value from href attribute
				Node tbody = link.child(0);
				for(int i=1;i<tbody.childNodes().size();i++) {
					Node node = tbody.childNode(i);
					//System.out.println(node);
					Stop stop = new Stop();
					stop.setId(Integer.parseInt(node.childNode(0).childNode(0).toString()));
					stop.setCode(node.childNode(1).childNode(0).toString());
					stop.setDesc(node.childNode(2).childNode(0).childNode(0).toString());
					stop.setArr(node.childNode(3).childNode(0).toString());
					stop.setDep(node.childNode(4).childNode(0).toString());
					stop.setHalt(node.childNode(5).childNode(0).toString());
					stop.setDist(node.childNode(6).childNode(0).toString());
					stop.setDay(node.childNode(7).childNode(0).toString());
					stops.add(stop);
				}
			}
		}
		return stops;
	}
}
