package com.holcim.altimetrik.android.utilities;

public class SFUser {
	private String _user_nick;
	private String _user_name;
	private String _user_email;
	private String _user_display_name;
	
	public String get_user_nick() { 
		return _user_nick; 
	}
	public void set_user_nick(String _user_nick) { 
		this._user_nick = _user_nick; 
	}
	public String get_user_name() {
		return _user_name;
	}
	public void set_user_name(String _user_name) {
		this._user_name = _user_name;
	}
	public String get_user_email() {
		return _user_email;
	}
	public void set_user_email(String _user_email) {
		this._user_email = _user_email;
	}
	public String get_user_display_name() {
		return _user_display_name;
	}
	public void set_user_display_name(String _user_display_name) {
		this._user_display_name = _user_display_name;
	}
}

