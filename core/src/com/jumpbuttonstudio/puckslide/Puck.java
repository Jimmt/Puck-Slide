package com.jumpbuttonstudio.puckslide;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Puck extends Image {
	Body body;

	public Puck(float x, float y, World world) {
		super(Textures.getTex("Object/puck.png"));

		setSize(getWidth() * Constants.SCALE, getHeight() * Constants.SCALE);

		BodyDef bd = new BodyDef();
		bd.type = BodyType.DynamicBody;
		bd.fixedRotation = true;
		bd.position.set(x - getWidth() / 2, y - getHeight() / 2);
		body = world.createBody(bd);
		FixtureDef fd = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(getWidth() / 2, getHeight() / 2);
		fd.shape = shape;
		fd.density = 1.0f;
		body.createFixture(fd);
		
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		
		if(Gdx.input.isKeyPressed(Keys.Q)){
			body.applyForceToCenter(1f, 0, true);
		}
		setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
	}

}
