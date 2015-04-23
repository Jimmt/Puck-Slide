package com.jumpbuttonstudio.puckslide;

public interface IGoogleServices {
	public void signIn();

	public void submitHighscore(int score);

	public void getLeaderboards();
	
	public void unlockAchievement(String id);

	public boolean getSignedIn();
}