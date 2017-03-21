package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;
//extends ApplicationAdapter
public class GotaG extends ApplicationAdapter  implements Screen{
	final Gota game;


	private Rectangle bucket;
	Texture bucketImage,dropImage;
	private Sound dropSound;
	private Music rainMusic;
	private OrthographicCamera camera;
	private Array<Rectangle> raindrops;
	private long lastDropTime;
	private int puntuacion;
	//Creo el objeto para añadir el texto en pantalla.
	BitmapFont font ;

	SpriteBatch batch;
	Texture gremlin;


	public GotaG(final Gota gam) {
		this.game = gam;
		gremlin = new Texture("gremlin.png");
		dropImage = new Texture("droplet.png");
		//Musica
		dropSound = Gdx.audio.newSound(Gdx.files.internal("waterdrop.wav"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("undertreeinrain.mp3"));
		//Se ejecuta la musica
		rainMusic.setLooping(true);
		//Camara
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		bucket = new Rectangle();
		bucket.x= 800 / 2 - 64/2;
		bucket.y = 20;
		bucket.width=64;
		bucket.height= 64;
		//Gotas de agua
		raindrops = new Array<Rectangle>();
		spawnRaindrop();


	}


	@Override
	public void render (float delta) {

		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		game.batch.draw(gremlin, bucket.x, bucket.y);
		for(Rectangle raindrop: raindrops) {
			game.batch.draw(dropImage, raindrop.x, raindrop.y);
		}
		//Creo el objeto que recibe por parametro el font  para escribir la puntacion en pantalla,
		// luego realizo dos casting primero conviento el int puntuacion en string
		//despues lo convierto en objecto CharSequence
		CharSequence puntos = (CharSequence) String.valueOf(puntuacion);
		//Añado la posición en la que voy a poner los la puntuación.
		///game.font.draw(batch,"Puntuación: " + puntos , 20, 440);
		game.batch.end();




		if (Gdx.input.isTouched()){
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			bucket.x = touchPos.x -64 / 2;
			//Movimiento de arriba hacia abajo con el ratón y el touch.
			bucket.y = touchPos.y -64 / 2;
		}
		//Movimiento con el teclado.
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) bucket.x -= 200 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) bucket.x += 200 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) bucket.y += 200 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) bucket.y -= 200 * Gdx.graphics.getDeltaTime();


		if(bucket.x < 0) bucket.x = 0;
		if(bucket.x > 800 - 64) bucket.x = 800 - 64;

		if(TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRaindrop();

		Iterator<Rectangle> iter = raindrops.iterator();

		while(iter.hasNext()) {
			Rectangle raindrop = iter.next();
			raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
			if(raindrop.y + 64 < 0) iter.remove();

			if(raindrop.overlaps(bucket)) {
				dropSound.play();
				iter.remove();
				create();
				//Sumo uno cada vez que recoge una gota.
				//Cuando una gota de llamo a metodo "CREATE()" que es el encargado de reinicar el juego
			}
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		gremlin.dispose();
		rainMusic.dispose();
		dropSound.dispose();
		dropImage.dispose();
	}

	private void spawnRaindrop() {
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0, 800-64);
		raindrop.y = 480;
		raindrop.width = 64;
		raindrop.height = 64;
		raindrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();
	}
	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {

		rainMusic.play();
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {

	}



}
