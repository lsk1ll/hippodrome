import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class HippodromeTest {
    @Test
    public void testConstructorWithNullName() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> new Hippodrome(null));
        assertEquals("Horses cannot be null.", exception.getMessage());
    }

    @Test
    public void testConstructorWithEmptyList() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> new Hippodrome(Collections.emptyList()));
        assertEquals("Horses cannot be empty.", exception.getMessage());
    }

    @Test
    public void testGetHorses() {
        List<Horse> horses = IntStream.range(0, 30)
                .mapToObj(i -> new Horse("Horse " + i,0,0))
                .collect(Collectors.toList());
        Hippodrome hippodrome = new Hippodrome(horses);
        List<Horse> returnedList = new ArrayList<>(hippodrome.getHorses());
        assertIterableEquals(horses, returnedList);
    }
    @Test
    public void testMoveAllHorses() {
        List<Horse> horses = IntStream.range(0,50)
                .mapToObj(i -> mock(Horse.class))
                .collect(Collectors.toList());
        horses.forEach(Horse::move);
        horses.forEach(horse -> verify(horse).move());
    }
    @Test
    public void testGetWinner() {
        List<Horse> horses = IntStream.range(0, 30)
                .mapToObj(i -> new Horse("Horse " + i,0, ThreadLocalRandom.current().nextDouble() * 100.0))
                .collect(Collectors.toList());
        Horse winner = horses.stream()
                .max(Comparator.comparing(Horse::getDistance))
                .get();
        Hippodrome hippodrome = new Hippodrome(horses);
        assertEquals(winner, hippodrome.getWinner());
    }
}
