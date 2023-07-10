import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
public class HorseTest {
    @Test
    public void testConstructorWithNullName() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> new Horse(null, 0));
        assertEquals("Name cannot be null.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\t", "\n"})
    public void testConstructorWithEmptyName(String name) {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> new Horse(name, 0));
        assertEquals("Name cannot be blank.", exception.getMessage());
    }

    @Test
    public void testConstructorWithNegativeSpeed() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () ->
                new Horse("Bucephalus", -2.4));

        assertEquals("Speed cannot be negative.", exception.getMessage());
    }

    @Test
    public void testConstructorWithNegativeDistance() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> new Horse("Bucephalus", 2.4, -10));

        assertEquals("Distance cannot be negative.", exception.getMessage());
    }

    @Test
    public void testGetName() {
        String name = "Bucephalus";
        double speed = 2.4;
        Horse horse = new Horse(name, speed);

        assertEquals(name, horse.getName());
    }

    @Test
    public void testGetSpeed() {
        String name = "Bucephalus";
        double speed = 2.4;
        Horse horse = new Horse(name, speed);

        assertEquals(speed, horse.getSpeed());
    }

    @Test
    public void testGetDistance() {
        String name = "Bucephalus";
        double speed = 2.4;
        double distance = 10.0;
        Horse horseWithDistance = new Horse(name, speed, distance);
        Horse horseWithoutDistance = new Horse(name, speed);

        assertAll("getDistance",
                () -> assertEquals(distance, horseWithDistance.getDistance(), "Distance doesn't match for horseWithDistance"),
                () -> assertEquals(0.0, horseWithoutDistance.getDistance(), "Distance should be 0 for horseWithoutDistance")
        );
    }

    @Test
    void testMoveCallsGetRandomDouble() {
        try (MockedStatic<Horse> mockedStatic = mockStatic(Horse.class)) {
            Horse horse = new Horse("Bucephalus", 2.4);

            horse.move();

            mockedStatic.verify(() -> Horse.getRandomDouble(0.2, 0.9));
        }
    }

    @ParameterizedTest
    @CsvSource({
            "0.5, 1.0",
            "1.0, 2.0",
            "2.0, 3.0"
    })
    public void testMoveCalculateDistance(double initialDistance, double speed) {
        try (MockedStatic<Horse> mockedStatic = mockStatic(Horse.class)){
            Horse horse = new Horse("Bucephalus", speed, initialDistance);
            mockedStatic.when(() -> Horse.getRandomDouble(0.2, 0.9)).thenReturn(0.4);
            double distance = initialDistance + speed * Horse.getRandomDouble(0.2,0.9);
            horse.move();
            assertEquals(distance, horse.getDistance());
        }
    }

}
