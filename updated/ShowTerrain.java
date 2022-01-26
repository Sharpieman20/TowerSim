import java.lang.Math;
import java.util.HashMap;
import java.util.*;


public class Run {

    public static final int MOB_CAP = 70;

    public static final int UPPER_MOB_CAP = 70;

    public static int total_spider_attempts = 0;

    public static int total_enderman_spawns = 0;

    public static int out_of_range = 0;

    public static int in_range = 0;

    public static int enderman_attempts_blocked_by_collision = 0;

    public static int spider_attempts_blocked_by_collision = 0;

    public static final double ENDERMAN_WEIGHT = 0.0244;

    public static final double SPIDER_WEIGHT = 0.24390243902;

    public static final int LEDGE_HEIGHT = 114;

    public static final double RADIUS = Math.sqrt(Math.max(0.0,Math.pow(128.0,2)-Math.pow(LEDGE_HEIGHT,2)));

    public static final int PACK_SIZE = 1;

    public static final double CHECK_HEIGHT = 79.0;

    public static final double SEE_RADIUS_DEC_TERRAIN = 55.0;

    public static final double SEE_RADIUS_MEH_TERRAIN = 49.0;

    public static final double SEE_RADIUS_BAD_TERRAIN = 45.0;

    public static final double SEE_RADIUS_GOD_TERRAIN = 60.0;

    public static final double SEE_ENDERMAN_RADIUS = SEE_RADIUS_GOD_TERRAIN;

    public static final double SEE_SPIDER_RADIUS = Math.sqrt(Math.max(0.0,Math.pow(78.93,2)-Math.pow(CHECK_HEIGHT,2)));

    public static int[][] mobArray = new int[256][256];

    public static final String SUPERFLAT = "Superflat";

    public static final String GOD_TERRAIN = "ConcentricFlatCircles";

    public static final String HILLY = "WeirdHilly";

    public static final String HILL = "HillTower";

    public static String TERRAIN_CHOICE = HILL;

    public static double total_ledge_time = 0.0;

    public static double TALL_GRASS_COVERAGE = 0.0;




    public static int getY(int x, int z) {

        if (TERRAIN_CHOICE.equals(SUPERFLAT)) {

            return 64;
        }

        if (TERRAIN_CHOICE.equals(GOD_TERRAIN)) {

            return (int) (64 + 2*Math.cos(x / 14.3) + 1*Math.sin(z / 26.9));
        }

        if (TERRAIN_CHOICE.equals(HILLY)) {

            return (int) (64 + 8*Math.cos(x / 7.15) + 16*Math.sin(z / 13.45));
        }

        if (TERRAIN_CHOICE.equals(HILL)) {

            int hill_width = 60;

            int hill_height = 24;

            if (Math.abs(x-128) > hill_width/2) {

                return 64;
            }

            if (Math.abs(z-128) > hill_width/2) {

                return 64;
            }

            return (int) (hill_height*Math.cos((Math.PI*(x-128))/hill_width) + 20*Math.cos((Math.PI*(z-128))/hill_width));
        }

        return 64;

        // double dist = Math.sqrt(Math.pow(x-128,2)+Math.pow(z-128,2));

        // double dist = Math.abs(x-128) + Math.abs(z-128);

        // if (dist < 20) {

        //     return 61;
        // }

        // if (dist < 29) {

        //     return 62;
        // }
        // if (dist < 38) {

        //     return 63;
        // }

        // if (dist < 47) {

        //     return 64;
        // }

        // if (dist < 56) {

        //     return 65;
        // }
        // if (dist < 65) {

        //     return 66;
        // }
        // return 67;
        // return 64;
    }

    public static void show() {
        
        int minY = 200;

        int width = 30;

        int height = 30;

        int center = 128;


        for (int x = center-height; x < center+height; x++) {

            for (int z = center-width; z < center+width; z++) {

                if (getY(x, z) < minY) {

                    minY = getY(x, z);
                }
            }
        }

        for (int x = center-height; x < center+height; x++) {

            for (int z = center-width; z < center+width; z++) {

                System.out.print((getY(x, z)-minY) % 10);
            }
            System.out.println();
        }
    }

    public static int getMob(int x, int z) {

        if (x < 0 || x >= 256) {

            return 0;
        }

        if (z < 0 || z >= 256) {

            return 0;
        }

        return mobArray[x][z];
    }

    public static void setMob(int x, int z, int val) {

        if (x < 0 || x >= 256) {

            return;
        }

        if (z < 0 || z >= 256) {

            return;
        }

        mobArray[x][z] = val;
    }

    public static int[] getNumEndermenInRoll() {

        int mobs = 0;

        int endermen = 0;

        mobArray = new int[256][256];

        int seen_endermen = 0;

        while (mobs <= UPPER_MOB_CAP) {

            double mob = Math.random();

            boolean spider = false;

            boolean enderman = false;

            if (mob < ENDERMAN_WEIGHT) {

                enderman = true;
            } else if (mob <= (ENDERMAN_WEIGHT+SPIDER_WEIGHT)) {

                spider = true;
            }

            // if (mob <= SPIDER_WEIGHT) {


            // }

            int numMobs = 0;

            int numTries = 0;

            int x = (int)(Math.random()*256);

            int z = (int)(Math.random()*256);

            int y = -1;

            packSpawn:
            while (numTries < PACK_SIZE) {

                numTries++;

                x += ((int)(Math.random()*6)) - ((int)(Math.random()*6));

                z += ((int)(Math.random()*6)) - ((int)(Math.random()*6));

                if (y == -1) {

                    y = getY(x, z);
                }

                if (Math.sqrt(Math.pow((x-128),2)+Math.pow(z-128,2)) > RADIUS) {

                    continue;
                }

                // check skylight

                if (15 > ((int)(32*Math.random()))) {

                    continue;
                }

                // check block light

                if (4<=((int)(8*Math.random()))) {

                    // good
                } else {

                    continue;
                }

                // check if block is same elevation

                if (y != getY(x, z)) {

                    continue;
                }

                if (x < 0 || x >= 256) {

                    continue;
                }

                if (z < 0 || z >= 256) {

                    continue;
                }

                if (spider) {

                    total_spider_attempts++;
                    spider_attempts_blocked_by_collision++;

                    for (int i = -1; i <= 1; i++) {

                        for (int j = -1; j <= 1; j++) {

                            if (getY(x+i, z+j) > y) {

                                continue packSpawn;
                            }

                            if (getMob(x+i,z+j) != 0) {

                                continue packSpawn;
                            }
                        }
                    }

                    spider_attempts_blocked_by_collision--;

                    for (int i = -1; i <= 1; i++) {

                        for (int j = -1; j <= 1; j++) {

                            setMob(x+i, z+j, 1);
                        }
                    }

                    // if (Math.sqrt(Math.pow((x-128),2)+Math.pow(z-128,2)) < SEE_SPIDER_RADIUS) {
                    //     seen_endermen++;
                    // }
                } else {

                    if (getMob(x, z) == 1) {
                        continue;
                    }

                    if (enderman) {

                        total_enderman_spawns++;

                        if (Math.sqrt(Math.pow((x-128),2)+Math.pow(z-128,2)) < SEE_ENDERMAN_RADIUS) {
                            endermen++;
                            seen_endermen++;
                            in_range++;
                        } else {

                            out_of_range++;
                        }
                    }

                    setMob(x, z, 1);
                }

                    

                numMobs++;
            }

            // if (mob <= ENDERMAN_WEIGHT) {

            //     endermen += numMobs;
            // }

            mobs += numMobs;
        }

        int[] results = new int[2];

        results[0] = seen_endermen;
        results[1] = endermen;

        return results;
    }


    static HashMap<Integer, Integer> binomTable = new HashMap<Integer, Integer>();

    static final int NUM_BINOM_LOOPS = 1_000;

    static final int BINOM_CHECK_PERCENTILE = 10;


    public static void populateBinomTable() {

        for (int i = 0; i <= 12; i++) {

            ArrayList<Integer> numEndysToGet = new ArrayList<Integer>();

            for (int j = 0; j < NUM_BINOM_LOOPS; j++) {

                int pearls = 0;

                int needed = 12 - i;

                int endys = 0;

                while (pearls < needed) {

                    endys++;

                    if (Math.random() < 0.5) {

                        pearls++;
                    }
                }

                numEndysToGet.add(endys);
            }

            Collections.sort(numEndysToGet);

            binomTable.put(i, numEndysToGet.get((int)(numEndysToGet.size()*BINOM_CHECK_PERCENTILE*0.01)));
        }
    }

public static void main(String[] args) {

    show();

    System.exit(0);

        int num_enderman_loops = 500_00;

        int num_tower_loops = 50_000;
        // int num_tower_loops = 0;

        // int num_inner = 4;
        // int threshold = 8;

        // double rate = 0.0196;

        // System.out.println((25_000.0*(MOB_CAP/70.0))/(Math.PI*Math.pow(RADIUS,2)));

        // System.out.println(RADIUS);
        // System.out.println(SEE_ENDERMAN_RADIUS);

        int total = 0;

        double total_time = 0.0;


        // int[] chances = new int[MOB_CAP+1];

        HashMap<Integer, Integer> cycleCounts = new HashMap<Integer, Integer>();

        double total_cycles = 0;
        double total_pearls = 0;
        double total_endermen_killed = 0.0;
        // double num_cycles = 0;

        int[][] samples = new int[num_enderman_loops][2];

        TreeMap<Integer, Integer> endyCounts = new TreeMap<Integer, Integer>();

        for(int i = 0; i < num_enderman_loops; i++) {

            samples[i] = getNumEndermenInRoll();

            if (!endyCounts.containsKey(samples[i][0])) {

                endyCounts.put(samples[i][0], 0);
            }

            endyCounts.put(samples[i][0], endyCounts.get(samples[i][0])+1);
        }

        for (Integer key : endyCounts.keySet()) {

            double val = ((double)endyCounts.get(key))/num_enderman_loops;

            System.out.println(String.format("%.10f",val));
        }
    }
}
