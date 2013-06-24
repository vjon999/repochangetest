package server;

public interface ProtocolConsts {
	
	public static final int DEFAULT_ADMIN_PORT = 11000;
	
	public static final String AUTHENTICATE = "authenticate";
	public static final String AUTHENTICATION_SUCCESSFUL = "authentication_success";
	public static final String AUTHENTICATION_FAIL = "authentication_failed";
	
	public static final String CLOSE_CONNECTION = "close_connection";
	public static final String GET_PORT = "get_port";
	public static final String CHANGE_PORT = "change_port";
	public static final String PORT = "port";
	
	public static final String ENGINE_NAME = "engine_name";
	public static final String AVAILABLE_ENGINES = "available_engines";
	public static final String GET_AVAILABLE_ENGINES = "get_available_engines";
	public static final String TEST_CONNECTION = "test_connection";
	public static final String SUCCESSFUL_CONNECTION_MESSAGE = "success";
	public static final String SUCCESSFUL_ENGINE_SELECTION_MESSAGE = "successfully_engine_selected";
	public static final String HELLO_PACKET = "helloPacket";
	
	public static final String DELIMITER = ",";
	
	public static final String SAVE_CONFIGURATION = "save_config";
	public static final String SAVE_SUCCESSFUL = "save_successful";
    public static final String SAVE_FAILED = "save_failed";
	public static final String SELECTED_ENGINE = "selected_engine";
	
	public static final String UNKNOWN_COMMAND = "unknown_command";
}
