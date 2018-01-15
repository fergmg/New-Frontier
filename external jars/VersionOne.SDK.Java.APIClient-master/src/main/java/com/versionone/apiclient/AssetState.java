package com.versionone.apiclient;

/**
 * VersionOne built in states for an Asset
 */
public enum AssetState {
	/**
	 * Asset has not been activated
	 */
	Future  (0),
	/**
	 * Asset is active
	 */
	Active  (64),
	/**
	 * Asset was closed
	 */
	Closed  (128),
	/**
	 * Asset is dead
	 */
	Dead    (192),
	/**
	 * Asset was deleted
	 */
	Deleted (255);
	
	private int _value;
	
	private AssetState(int value) {_value = value;}
	
	/**
	 * Get the integer value of this state
	 * 
	 * @return int
	 */
	public int value() {return _value;}
	
	/**
	 * Select AssetState based on an integer value
	 * 
	 * @param intValue - int
	 * @return AssetState
	 */
	public static AssetState valueOf(int intValue) {		
		AssetState[] temp = AssetState.values();
		for(AssetState oneState : temp) {
			if(oneState._value == intValue)
				return oneState;
		}
		throw new IllegalArgumentException(intValue + " is not a valid AssetType value");
	}
	
	/**
	 * Is an int a valid AssetState
	 * 
	 * @param intValue - int
	 * @return boolean - boolean
	 */
	public static boolean isDefined(int intValue) {
		AssetState[] temp = AssetState.values();
		for(AssetState oneState : temp) {
			if(oneState._value == intValue)
				return true;
		}
		return false;
	}	
}