package com.jumpbuttonstudio.puckslide;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class GroundTile extends Image {
	Body body;

	enum TileType {
		MUD("Tiles/02.png", 1.0f), ICE_FLAT("Tiles/01.png", 0.0f), ICE_LEFT("Tiles/00.png", 0.0f), ICE_RIGHT(
				"Tiles/03.png", 0.0f);

		private String path;
		private float friction;

		TileType(String path, float friction) {
			this.path = path;
			this.friction = friction;
		}
	}

	public GroundTile(float x, TileType type, World world) {
		super(Textures.getTex(type.path));

		setSize(getWidth() * Constants.SCALE, getHeight() * Constants.SCALE);

		BodyDef bd = new BodyDef();
		bd.type = BodyType.StaticBody;
		bd.position.set(x - getWidth() / 2, getHeight() / 2);
		body = world.createBody(bd);
		FixtureDef fd = new FixtureDef();
		fd.friction = type.friction;
		EdgeShape shape = new EdgeShape();
		shape.set(-getWidth() / 2, getHeight() / 2, getWidth() / 2, getHeight() / 2);
//		PolygonShape shape = new PolygonShape();
//		shape.setAsBox(getWidth() / 2, getHeight() / 2);
		fd.shape = shape;
		body.createFixture(fd);

	}

	@Override
	public void act(float delta) {
		super.act(delta);

		setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
	}

}
