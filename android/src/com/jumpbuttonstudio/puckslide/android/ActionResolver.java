package com.jumpbuttonstudio.puckslide.android;

public interface ActionResolver {
	public boolean getSignedInGPGS();

	public void loginGPGS();
	
	public void logoutGPGS();

	public void submitScoreGPGS(int score);

	public void unlockAchievementGPGS(String achievementId);

	public void getLeaderboardGPGS();

	public void getAchievementsGPGS();
	

}
