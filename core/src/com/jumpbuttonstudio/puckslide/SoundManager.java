package com.jumpbuttonstudio.puckslide;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ObjectMap.Entries;
import com.badlogic.gdx.utils.ObjectMap;

public class SoundManager {
	ObjectMap<String, Sound> sounds;
	ObjectMap<String, Music> musics;

	private boolean play = true;

	public SoundManager() {
		sounds = new ObjectMap<String, Sound>();
		musics = new ObjectMap<String, Music>();
	}

	public void play(String name) {
		if (play)
			((Sound) sounds.get(name)).play();
	}

	public void playMusic(String name) {
		((Music) musics.get(name)).setVolume(1f);
		if (play) {
			Entries<String, Music> entries = musics.entries();
			while (entries.hasNext()) {
				entries.next().value.pause();
			}
			((Music) musics.get(name)).play();
		}
	}

	public void stopMusic(String name) {
		if (play)
			((Music) musics.get(name)).stop();
	}

	public void play(String name, float volume) {
		if (play)
			((Sound) sounds.get(name)).play(volume);
	}

	public void playRandom(String name, float volume, float min, float max) {
		if (play)
			((Sound) sounds.get(name)).play(volume, min + MathUtils.random(max - min), 0);
	}

	public void playMusic(String name, float volume) {
		if (play) {
			Entries<String, Music> entries = musics.entries();
			while (entries.hasNext()) {
				entries.next().value.pause();
			}
			((Music) musics.get(name)).setVolume(volume);
			((Music) musics.get(name)).play();
		}
	}

	public void loop(String name) {
		if (play)
			((Sound) sounds.get(name)).loop();
	}

	public void loadSound(String name, FileHandle file) {
		Sound sound = Gdx.audio.newSound(file);
		sounds.put(name, sound);
	}

	public void loadMusic(String name, FileHandle file) {
		Music music = Gdx.audio.newMusic(file);
		musics.put(name, music);
	}

	public void setPlay(boolean play) {
		this.play = play;

		if (!play) {
			Entries<String, Music> entries = musics.entries();
			while (entries.hasNext()) {
				entries.next().value.pause();
			}
		}
	}

	public boolean getPlay() {
		return play;
	}
}
