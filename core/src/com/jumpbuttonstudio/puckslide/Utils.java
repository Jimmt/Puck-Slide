package com.jumpbuttonstudio.puckslide;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;

public class Utils {

	public static void scaleEffect(ParticleEffect effect) {
		ParticleEmitter emitter = effect.getEmitters().get(0);
		emitter.getScale().setHigh(emitter.getScale().getHighMax() * Constants.SCALE);
		emitter.getVelocity().setHigh(emitter.getVelocity().getHighMin() * Constants.SCALE,
				emitter.getVelocity().getHighMax() * Constants.SCALE);
		emitter.getYOffsetValue().setLow(emitter.getYOffsetValue().getLowMin() * Constants.SCALE,
				emitter.getYOffsetValue().getLowMax() * Constants.SCALE);
		emitter.getXOffsetValue().setLow(emitter.getXOffsetValue().getLowMin() * Constants.SCALE,
				emitter.getXOffsetValue().getLowMax() * Constants.SCALE);
		emitter.getWind().setHigh(emitter.getWind().getHighMin() * Constants.SCALE,
				emitter.getWind().getHighMax() * Constants.SCALE);
	}
}
