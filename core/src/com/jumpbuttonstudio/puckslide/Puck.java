package com.jumpbuttonstudio.puckslide;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Puck extends Image {
	ParticleEffect puckBlur, mudBlur, snowBlur;
	Body body;
	boolean launched;

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
		shape.setAsBox(0.29f / 2, getHeight() / 2);
		fd.shape = shape;
		fd.density = 0.5f;
		fd.friction = 1.0f;
		body.createFixture(fd);
		body.setUserData(new UserData("puck", this));

		setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);

		puckBlur = new ParticleEffect();
		puckBlur.load(Gdx.files.internal("Effect/puckblur.p"), Gdx.files.internal("Object/"));
		mudBlur = new ParticleEffect();
		mudBlur.load(Gdx.files.internal("Effect/mudblur.p"), Gdx.files.internal("Effect/"));
		mudBlur.getEmitters().get(0).getXOffsetValue().setLow(-getWidth() / 2);
		snowBlur = new ParticleEffect();
		snowBlur.load(Gdx.files.internal("Effect/snowblur.p"), Gdx.files.internal("Effect/"));
		snowBlur.getEmitters().get(0).getXOffsetValue().setLow(-getWidth() / 2);

		Utils.scaleEffect(puckBlur);
		Utils.scaleEffect(mudBlur);
		Utils.scaleEffect(snowBlur);

		mudBlur.allowCompletion();
		snowBlur.allowCompletion();
// snowBlur.start();
// mudBlur.start();
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		
		

		puckBlur.update(delta);
		mudBlur.update(delta);
		snowBlur.update(delta);
		setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);

		if (body.getLinearVelocity().x <= 0.2f) {
			puckBlur.allowCompletion();

		} else {
			puckBlur.start();
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);

		puckBlur.setPosition(getX() + getWidth() / 2, getY() + getHeight() / 2);
		mudBlur.setPosition(getX(), getY() + getHeight() / 2);
		snowBlur.setPosition(getX(), getY() + getHeight() / 2);
		puckBlur.draw(batch);
		snowBlur.draw(batch);
		mudBlur.draw(batch);

		if (!launched) {
			snowBlur.allowCompletion();
			mudBlur.allowCompletion();
		}

	}

}
