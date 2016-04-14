package photoman.service;

import java.io.Serializable;


public interface PreferenceService {

	public void setPreference(String prefName, Serializable prefValue);

	public Serializable getPreference(String prefName);

}
