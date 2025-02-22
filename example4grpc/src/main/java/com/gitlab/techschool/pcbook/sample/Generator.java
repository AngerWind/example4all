package com.gitlab.techschool.pcbook.sample;

import com.gitlab.techschool.pcbook.pb.*;
import com.google.protobuf.Timestamp;

import java.time.Instant;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;


/**
 * 该类用于生成随机的实体类
 */
public class Generator {
    private Random rand;

    public Generator() {
        rand = new Random();
    }

    public Keyboard NewKeyboard() {
        return Keyboard.newBuilder()
                .setLayout(randomKeyboardLayout())
                .setBacklit(rand.nextBoolean())
                .build();
    }

    public CPU NewCPU() {
        String brand = randomCPUBrand();
        String name = randomCPUName(brand);

        int numberCores = randomInt(2, 8);
        int numberThreads = randomInt(numberCores, 12);

        double minGhz = randomDouble(2.0, 3.5);
        double maxGhz = randomDouble(minGhz, 5.0);

        return CPU.newBuilder()
                .setBrand(brand)
                .setName(name)
                .setNumberCores(numberCores)
                .setNumberThreads(numberThreads)
                .setMinGhz(minGhz)
                .setMaxGhz(maxGhz)
                .build();
    }

    public GPU NewGPU() {
        String brand = randomGPUBrand();
        String name = randomGPUName(brand);

        double minGhz = randomDouble(1.0, 1.5);
        double maxGhz = randomDouble(minGhz, 2.0);

        Memory memory = Memory.newBuilder()
                .setValue(randomInt(2, 6))
                .setUnit(Memory.Unit.GIGABYTE)
                .build();

        return GPU.newBuilder()
                .setBrand(brand)
                .setName(name)
                .setMinGhz(minGhz)
                .setMaxGhz(maxGhz)
                .setMemory(memory)
                .build();
    }

    public Memory NewRAM() {
        return Memory.newBuilder()
                .setValue(randomInt(4, 64))
                .setUnit(Memory.Unit.GIGABYTE)
                .build();
    }

    public Storage NewSSD() {
        Memory memory = Memory.newBuilder()
                .setValue(randomInt(128, 1024))
                .setUnit(Memory.Unit.GIGABYTE)
                .build();

        return Storage.newBuilder()
                .setDriver(Storage.Driver.SSD)
                .setMemory(memory)
                .build();
    }

    public Storage NewHDD() {
        Memory memory = Memory.newBuilder()
                .setValue(randomInt(1, 6))
                .setUnit(Memory.Unit.TERABYTE)
                .build();

        return Storage.newBuilder()
                .setDriver(Storage.Driver.HDD)
                .setMemory(memory)
                .build();
    }

    public Screen NewScreen() {
        int height = randomInt(1080, 4320);
        int width = height * 16 / 9;

        Screen.Resolution resolution = Screen.Resolution.newBuilder()
                .setHeight(height)
                .setWidth(width)
                .build();

        return Screen.newBuilder()
                .setSizeInch(randomFloat(13, 17))
                .setResolution(resolution)
                .setPanel(randomScreenPanel())
                .setMultitouch(rand.nextBoolean())
                .build();
    }

    public Laptop NewLaptopWithoutId() {
        String brand = randomLaptopBrand();
        String name = randomLaptopName(brand);

        double weightKg = randomDouble(1.0, 3.0);
        double priceUsd = randomDouble(1500, 3500);

        int releaseYear = randomInt(2015, 2019);

        return Laptop.newBuilder()
                .setBrand(brand)
                .setName(name)
                .setCpu(NewCPU())
                .setRam(NewRAM())
                .addGpus(NewGPU())
                .addStorages(NewSSD())
                .addStorages(NewHDD())
                .setScreen(NewScreen())
                .setKeyboard(NewKeyboard())
                .setWeightKg(weightKg)
                .setPriceUsd(priceUsd)
                .setReleaseYear(releaseYear)
                .setUpdatedAt(timestampNow())
                .build();
    }

    public Laptop NewLaptopWithId(String id) {
        return NewLaptopWithoutId().toBuilder().setId(id).build();
    }

    public Laptop NewLaptopWithRandomUUID() {
        return NewLaptopWithId(UUID.randomUUID().toString());
    }

    public double NewLaptopScore() {
        return randomInt(1, 10);
    }

    private Timestamp timestampNow() {
        Instant now = Instant.now();
        return Timestamp.newBuilder()
                .setSeconds(now.getEpochSecond())
                .setNanos(now.getNano())
                .build();
    }

    private String randomLaptopName(String brand) {
        return switch (brand) {
            case "Apple" -> randomStringFromSet("Macbook Air", "Macbook Pro");
            case "Dell" -> randomStringFromSet("Latitude", "Vostro", "XPS", "Alienware");
            default -> randomStringFromSet("Thinkpad X1", "Thinkpad P1", "Thinkpad P53");
        };
    }

    private String randomLaptopBrand() {
        return randomStringFromSet("Apple", "Dell", "Lenovo");
    }

    private Screen.Panel randomScreenPanel() {
        if (rand.nextBoolean()) {
            return Screen.Panel.IPS;
        }
        return Screen.Panel.OLED;
    }

    private float randomFloat(float min, float max) {
        return min + rand.nextFloat() * (max - min);
    }

    private String randomGPUName(String brand) {
        if (Objects.equals(brand, "NVIDIA")) {
            return randomStringFromSet(
                    "RTX 2060",
                    "RTX 2070",
                    "GTX 1660-Ti",
                    "GTX 1070"
            );
        }

        return randomStringFromSet(
                "RX 590",
                "RX 580",
                "RX 5700-XT",
                "RX Vega-56"
        );
    }

    private String randomGPUBrand() {
        return randomStringFromSet("NVIDIA", "AMD");
    }

    private double randomDouble(double min, double max) {
        return min + rand.nextDouble() * (max - min);
    }

    private int randomInt(int min, int max) {
        return min + rand.nextInt(max - min + 1);
    }

    private String randomCPUName(String brand) {
        if (Objects.equals(brand, "Intel")) {
            return randomStringFromSet(
                    "Xeon E-2286M",
                    "Core i9-9980HK",
                    "Core i7-9750H",
                    "Core i5-9400F",
                    "Core i3-1005G1"
            );
        }

        return randomStringFromSet(
                "Ryzen 7 PRO 2700U",
                "Ryzen 5 PRO 3500U",
                "Ryzen 3 PRO 3200GE"
        );
    }

    private String randomCPUBrand() {
        return randomStringFromSet("Intel", "AMD");
    }

    private String randomStringFromSet(String... a) {
        int n = a.length;
        if (n == 0) {
            return "";
        }
        return a[rand.nextInt(n)];
    }

    private Keyboard.Layout randomKeyboardLayout() {
        return switch (rand.nextInt(3)) {
            case 1 -> Keyboard.Layout.QWERTY;
            case 2 -> Keyboard.Layout.QWERTZ;
            default -> Keyboard.Layout.AZERTY;
        };
    }

    public static void main(String[] args) {
        Generator generator = new Generator();
        Laptop laptop = generator.NewLaptopWithId(UUID.randomUUID().toString());
        System.out.println(laptop);
    }
}
