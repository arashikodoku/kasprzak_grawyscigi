package pl.edu.kasprzak.gdxhellow;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GdxHello extends ApplicationAdapter {
	SpriteBatch batch;
	OrthographicCamera mapCamera;
	OrthographicCamera carCamera;
	Texture mapa;
	Texture grid;
	Texture car;
	float rotationD;
	float rotationSpeed;
	float forward = 0;
	float srotdeg = 0;
	float forwardD = 100;
	float srotrad = 0;
	float x = 800;
	float y = 800;

	@Override
	public void create () {
		batch = new SpriteBatch();
		mapCamera = new OrthographicCamera();
		carCamera = new OrthographicCamera();
		mapa = new Texture("mapa.png");
		grid = new Texture("libgdxgridtest.png");
		car = new Texture("Audi.png");
		rotationD = 0;
		rotationSpeed = 50;


	}

	float time = 0;

	@Override
	public void render () {
		// Na razie symulejemy ruch gracza korzystając z funkcji trygonomoetrycznych i czasu


		// Skanujemy dotyk dla maksymalnie 10 palców
		for (int i = 0; i < 10; ++i) {

			if (Gdx.input.isTouched(i)) {
				Gdx.app.log("TOUCH", "touch ID: " + i + " x: " + Gdx.input.getX(i) +
						" y: " + Gdx.input.getY(i));
				if (Gdx.input.getX(i) > Gdx.graphics.getWidth() * 0.6) {
					Gdx.app.log("TOUCH", "rIGHT");
					rotationD = rotationSpeed;
				}
				if (Gdx.input.getX(i) < Gdx.graphics.getWidth() * 0.4) {
					Gdx.app.log("TOUCH", "LEFT");
					rotationD =- rotationSpeed;
				}
			}


		}
		// Zmienna time oznacza czas gry - getDeltaTime zwraca różnicę czasu
		time += Gdx.graphics.getDeltaTime();
		float radius = 200;
		float centerX = 800;
		float centerY = 800;
		srotdeg -= rotationD * Gdx.graphics.getDeltaTime(); // Czas traktujemy jako radiany a obroty wymagają stopni
		forward = forwardD * Gdx.graphics.getDeltaTime();
		srotrad = (float) (srotdeg / 180 * Math.PI);
		x += Math.cos(srotrad) * forward;
		y += Math.sin(srotrad) * forward;

		// Czyścimy ekran - 0, 0, 0 da czarny, ale na razie jest zielony żeby grid było widać
		Gdx.gl.glClearColor(0, 1, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Ustawienie podstawowych parametrów kamery dla mapy
		mapCamera.setToOrtho(false, 800, 480); // Rozmiar wirtualnego ekranu
		mapCamera.translate(-400, -240);       // Przestawiamy na środek ekranu początek dla kamery
		// Przesuwamy kamerę żeby widziała aktualne miejsce gdzie jest gracz
		mapCamera.translate(x, y);
		// Kamera przelicza swoje parametry
		mapCamera.update();

		// Ustawienie podstawowych parametrów kamery dla samochodu
		carCamera.setToOrtho(false, 800, 480); // Rozmiar wirtualnego ekranu
		carCamera.translate(-400, -240);       // Przestawiamy na środek ekranu początek dla kamery
		carCamera.translate(25, 25);           // Przestawiamy środek obrotu na środek samochodu
		carCamera.rotate(srotdeg - 90);             // Obracamy samochód zgodnie z kierunkiem jazdy
		carCamera.update();


		// Grupa rysowanych obiektów
		batch.begin();
		{
			// Ustawiamy kamerę dla mapy
			batch.setProjectionMatrix(mapCamera.combined);
			// Mapa
			batch.draw(mapa, 0, 0);
			// Pomocnicza siatka ze współrzędnymi
			batch.draw(grid, 0, 0);

			// Ustawiamy kamerę dla samochodu
			batch.setProjectionMatrix(carCamera.combined);
			// Rysujemy samochód
			batch.draw(car, 0, 0);
		}
		batch.end();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		mapa.dispose();
	}
}
