package com.jumpbuttonstudio.puckslide;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class GameContactListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		UserData a = (UserData) contact.getFixtureA().getBody().getUserData();
		UserData b = (UserData) contact.getFixtureB().getBody().getUserData();

		if (a.tag.equals("mud") && b.tag.equals("puck")) {
			if (((Puck) b.obj).launched) {
				((Puck) b.obj).mudBlur.start();
			}

		}

		if (a.tag.equals("ice") && b.tag.equals("puck")) {
			if (((Puck) b.obj).launched) {
				((Puck) b.obj).snowBlur.start();
			}

		}
	}

	@Override
	public void endContact(Contact contact) {
		UserData a = (UserData) contact.getFixtureA().getBody().getUserData();
		UserData b = (UserData) contact.getFixtureB().getBody().getUserData();

		if (a.tag.equals("mud") && b.tag.equals("puck")) {
			((Puck) b.obj).mudBlur.allowCompletion();

		}

		if (a.tag.equals("ice") && b.tag.equals("puck")) {
			((Puck) b.obj).snowBlur.allowCompletion();

		}

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

}
