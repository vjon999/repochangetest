package com.prapps.rail.helper;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.prapps.rail.KVPair;
import com.prapps.rail.Station;
import com.prapps.rail.Stop;
import com.prapps.rail.Train;

public class Test {
	
	FileOp fileOp = new FileOp();
	Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public static void main(String[] args) throws Exception {
		Test test = new Test();
		//test.updateDB();
		//test.loadAllStations();
		//test.toJson();
		test.updateTrainStop();
		/*int trainNo = 33529;		
		test.updateTrain(trainNo);*/
		//test.saveStationCodes();
		//test.updateAllTrains();
		
		/*File file = new File("/home/pratik/sdahtrains.txt");
		FileInputStream is = new FileInputStream(file);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String line = null;
		Station stn = new Station();
		stn.setCode("SDAH");
		Collection<Train> trains = new ArrayList<Train>();
		Collection<Integer> trainNos = new ArrayList<Integer>();
		while((line = reader.readLine()) != null) {
			String[] bits = line.split("\t");
			String trainNumber = bits[1];
			String type = bits[3];
			String arr = bits[4];
			String dep = bits[5];
			String rundays = bits[7];
			String from = bits[2].split(" ")[0];
			String to = bits[2].split(" ")[1];
			Train train = null;
				train = new Train(Integer.parseInt(trainNumber), from, to);
				train.setType(type);
				train.setArr(arr);
				train.setDep(dep);
				train.setRundays(rundays);
				trains.add(train);
				trainNos.add(Integer.parseInt(trainNumber));
		}
		stn.setTrains(trainNos);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(stn);
		//System.out.println(json);
		FileOp fileop = new FileOp();
		fileop.save(json, "data/Stations.json");
		
		json = gson.toJson(trains);
		System.out.println(json);
		fileop.save(json, "data/Trains.json");*/
	}
	
	public void updateAllTrains() throws IOException {
		Collection<Train> trains = fileOp.loadTrains();
		int ctr = 1000;
		for(Train train : trains) {
			if(train.getRoute() == null) {
				try {
					String html = fileOp.getRoute(train);
					fileOp.save(html, "data/"+train.getId()+".html");
					//if(new File("data/"+train.getNo()+".html").exists()) {
						//String html = fileOp.readFile("data/"+train.getNo()+".html");
						Collection<Stop> stops = fileOp.parseHTML(html);
						train.setRoute(stops);
						try {
							Thread.sleep((ctr+100)%3000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					//}
				}
				catch(IOException ex) {
					ex.printStackTrace();
				}
				
			}
		}
		String json = gson.toJson(trains);
		fileOp.save(json, "data/Trains.json");
	}
	
	public void updateTrain(String trainNo) throws IOException {
		Collection<Train> trains = fileOp.loadTrains();
		Train train = null;
		for(Train t : trains) {
			if(trainNo.equals(t.getId())) {
				train = t;
				break;
			}
		}
		String data = fileOp.readFile("data/"+trainNo);
		String[] lines = data.split("\n");
		Collection<Stop> stops = new ArrayList<Stop>();
		Stop stop = null;
		for(String line : lines) {
			System.out.println(line);
			String[] attrs = line.split("\t");
			stop = new Stop();
			stop.setId(Integer.parseInt(attrs[0]));
			stop.setCode(attrs[1]);
			stop.setDesc(attrs[2]);
			stop.setArr(attrs[3]);
			stop.setDep(attrs[4]);
			stop.setHalt(attrs[5]);
			stop.setDist(attrs[6]);
			stop.setDay(attrs[7]);
			stops.add(stop);
		}
		train.setRoute(stops);
		String json = gson.toJson(trains);
		fileOp.save(json, "data/Trains.json");
	}
	
	/*public void saveStationCodes() throws IOException {
		String data = fileOp.readFile("data/stationCodes1");
		String[] stns = data.split(",");
		Map<String,String> stnMap = new HashMap<String, String>();
		for(int i=0;i<stns.length;i=i+2) {
			if(stnMap.containsKey(stns[i])) {
				stnMap.put(stns[i], stnMap.get(stns[i])+","+stns[i+1]);
			}
			else {
				stnMap.put(stns[i], stns[i+1]);
			}
		}
		System.out.println(stnMap);
		Collection<KVPair> kvPairs = new ArrayList<KVPair>();
		for(Entry<String,String> entry : stnMap.entrySet()) {
			KVPair kvPair = new KVPair(entry.getKey(), entry.getValue());
			kvPairs.add(kvPair);
		}
		String json = gson.toJson(kvPairs);
		fileOp.save(json, "data/StationCodes.json");
	}*/
	
	public void loadAllStations() throws Exception {
		Collection<KVPair> stations = fileOp.loadAllStations();
		/*for(KVPair stn : stations) {
			System.out.println("INSERT INTO a7054067_apps.STATION(CODE,NAME) VALUES('"+stn.getKey()+"','"+stn.getValue()+"');");
		}*/
		
		DBConnector conn = new DBConnector();
		ResultSet rs = conn.query("select * from a7054067_apps.STATION");
		Map<String,Integer> map = new HashMap<String,Integer>();
		while(rs.next()) {
			map.put(rs.getString("CODE"), rs.getInt("ID"));
		}
		conn.close();
		
		Collection<Train> trains = fileOp.loadTrains();
		Map<Integer, Collection<Stop>> trainStopMap = new HashMap<Integer, Collection<Stop>>();
		for(Train stn : trains) {
			//System.out.println("INSERT INTO a7054067_apps.TRAIN(ID,TYPE,RUNDAYS,CLASSES) VALUES("+stn.getNo()+",'"+stn.getType()+"','"+stn.getRundays()+"',"+stn.getClasses()+");");
			if(null != stn.getRoute()) {
				List<Stop> stops = (List<Stop>) stn.getRoute();
				Collections.sort(stops);
				int seq = 0;
				for(Stop stop : stops) {
					int id = map.get(stop.getCode().trim());
					System.out.println("INSERT INTO a7054067_apps.TRAIN_STOP(TRAIN_ID,STATION_ID,ARR,DEP,HALT,DAY,DIST,SEQ) "
							+ "VALUES("+stn.getId()+","+id+",'"+stop.getArr()+"','"+stop.getDep()+"','"+stop.getHalt().trim()+"','"+stop.getDay()+"',"+stop.getDist().trim()+","+ seq++ +");");
				}
			}
		}
	}
	
	public void updateDB() throws Exception {
		DBConnector conn = new DBConnector();
		ResultSet rs = conn.query("select * from a7054067_apps.TRAIN_STOP");
		while(rs.next()) {
			System.out.println(rs.getInt("ID")+"\t"+rs.getInt("TRAIN_ID"));
		}
		conn.close();
		/*select a.TRAIN_ID,c.NAME, d.NAME,a.DEP,b.ARR,(b.DIST-a.DIST) as dist from a7054067_apps.TRAIN_STOP a,a7054067_apps.TRAIN_STOP b, a7054067_apps.STATION c, a7054067_apps.STATION d
		 where a.TRAIN_ID=b.TRAIN_ID and c.ID=a.STATION_ID and d.ID=b.STATION_ID and a.STATION_ID=5390 and b.STATION_ID=688 and a.SEQ<b.SEQ;*/
	}
	
	public void updateTrainStop() {
		Collection<Train> trains = fileOp.loadTrains();
		Collection<Station> stations = fileOp.loadJson("stations.json");
		Map<String, Train> trainMap = new HashMap<String, Train>();
		for(Train train : trains) {
			trainMap.put(train.getId(), train);
		}
		Map<Integer, Station> StationMap = new HashMap<Integer, Station>();
		for(Station station : stations) {
			StationMap.put(station.getId(), station);
		}
		Collection<Stop> stops = fileOp.loadAllStops();
		Train train = null;
		for(Stop stop : stops) {
			//System.out.println(stop);
			if(null != stop) {
				if(stop.getSeq() == 0) {
					train = trainMap.get(stop.getTrainId()+"");
					Station station = StationMap.get(stop.getStationId());
					//System.out.println(station);
					train.setFrom(station.getDesc());
				}
				else if(stop.getDep().equals("Last")) {
					train = trainMap.get(stop.getTrainId()+"");
					Station station = StationMap.get(stop.getStationId());
					train.setTo(station.getDesc());
					if(station.getId() == 519) {
						System.out.println(train);
					}
					
				}	
			}	
		}
		
		for(Entry<String, Train> kv :  trainMap.entrySet()) {
			//System.out.println(kv.getValue());
		}
		
	}
	
	public void toJson() throws Exception {
		DBConnector conn = new DBConnector();
		ResultSet rs = conn.query("SELECT * FROM a7054067_apps.STATION;");
		StringBuilder sb = new StringBuilder("var stations = [");
		while(rs.next()) {
			sb.append(
					"{\"id\": \""+rs.getInt("ID")+"\","
					+"\"type\": \""+rs.getString("CODE")+"\","
					+"\"name\": \""+rs.getString("NAME")+"\"},\n");
			//break;
		}
		sb.append("];");
		conn.close();
		fileOp.save(sb.toString(), "stations.json");
		//System.out.println(sb);
	}

}
