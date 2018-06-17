package graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import mainAlgorithms.ShortestPathHeuristicV2;
import mainAlgorithms.SteinerTreeSolver;

public class RandomMain {

	public static void main(String[] args) throws InterruptedException {
		// Scanner in = new Scanner(System.in);
		// final CountDownLatch exit_now = new CountDownLatch(1);
		// double worker = 0.0;
		// int n;
		//
		// SignalHandler termHandler = new SignalHandler() {
		// @Override
		// public void handle(Signal sig) {
		// System.out.println("Terminating");
		// exit_now.countDown();
		// }
		// };
		// Signal.handle(new Signal("TERM"), termHandler);
		//
		// n = in.nextInt();
		// for (int i = 0; i < n && exit_now.getCount() == 1; i++) {
		// worker += Math.sqrt(i);
		// }
		// System.out.print((int) (worker / n));

		shortestPathHeuristicV2();
		System.out.println("\n");
		shortestPathHeuristicV2wPP();
		// writeArticulationPointsToFile();
	}

	public static void shortestPathHeuristicV2() {
		File[] files = readFiles(new File("data\\exact\\instance003.gr"));
		for (int i = 0; i < files.length; i++) {
			System.out.println(files[i].getParent() + "\\" + files[i].getName());
			SteinerTreeSolver solver = new ShortestPathHeuristicV2();

			UndirectedGraph graph = new UndirectedGraphReader().read(files[i]);

			List<Edge> result = solver.solve(graph);
		}
	}

	public static void shortestPathHeuristicV2wPP() {
		File[] files = readFiles(new File("data\\exact\\instance003.gr"));
		for (int i = 0; i < files.length; i++) {
			System.out.println(files[i].getParent() + "\\" + files[i].getName());
			SteinerTreeSolver solver = new ShortestPathHeuristicV2();

			UndirectedGraph graph = new UndirectedGraphReader().read(files[i]);
			PreProcess processed = new PreProcess(graph);
			boolean[] preProcessable;
			do {
				preProcessable = processed.graph.preProcessable();
				// pp.rangeCheck();
				if (preProcessable[0]) {
					processed.removeLeafNodes();
				}
				if (preProcessable[1]) {
					processed.removeNonTerminalDegreeTwo();
				}
			} while (preProcessable[0] || preProcessable[1]);

			List<Edge> result = solver.solve(processed.graph);
		}
	}

	public static void writeArticulationPointsToFile() {
		File[] files = readFiles(new File("data\\exact"));
		int[][] info = new int[files.length][5];
		for (int i = 0; i < files.length; i++) {
			System.out.println(i);
			UndirectedGraph graph = new UndirectedGraphReader().read(files[i]);
			info[i][0] = graph.getVertices().size();
			info[i][1] = graph.getEdges().size();
			info[i][2] = graph.getNumberOfTerminals();
			PreProcess ppGraph = new PreProcess(graph);
			long start = System.currentTimeMillis();
			info[i][3] = ppGraph
					.articulationPointFinding(graph.getVertices().get(graph.getVertices().keySet().toArray()[0]), graph.getVertices().size()).size();
			info[i][4] = (int) (System.currentTimeMillis() - start);
		}
		System.out.println("\n\n Writing Total results:");
		try {
			PrintWriter writer = new PrintWriter("E:\\Programming\\Java\\ProjectSteinerTreeJava\\res\\artiPointsExact.txt", "UTF-8");
			// tms = time in ms
			writer.println("FileIndex, #Vertices, #Edges, #Terminals, #ArtiPoints, Time(ms)");
			String cur = "";
			for (int i = 0; i < info.length; i++) {
				cur = Integer.toString(i + 1);
				for (int j = 0; j < info[i].length; j++) {
					cur += ", " + Integer.toString(info[i][j]);
				}
				writer.println(cur);
			}
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Reads all files from a given directory. The directory is allowed to be both a
	 * folder or a file.
	 *
	 * @param directory
	 *            The directory for which file(s) have to be read.
	 * @return An array of found files.
	 *
	 * @author Joshua Scheidt
	 */
	private static File[] readFiles(File directory) {
		if (directory.exists()) {
			if (directory.isFile() && directory.getName().contains(".gr")) {
				return new File[] { directory };
			} else if (directory.isDirectory()) {
				File[] files = directory.listFiles();
				ArrayList<File> filesList = new ArrayList<>();
				for (int i = 0; i < files.length; i++) {
					if (files[i].isFile() && files[i].getName().contains(".gr")) {
						filesList.add(files[i]);
					}
				}
				return filesList.toArray(files);
			}
		}
		return new File[] {};
	}

}