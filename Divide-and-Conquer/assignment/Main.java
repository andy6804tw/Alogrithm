import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner scn = new Scanner(System.in);
		List<Integer> list; // 建立一個整數形態的List串列
		while (true) {
			list = new ArrayList<>(); // 建立一個空的ArrayList容器
			System.out.println("Please input Integer list:");
			String str = scn.nextLine();
			String arr[] = str.split(" ");
			for (int i = 0; i < arr.length; i++) {
				try {
					int num = Integer.parseInt(arr[i]);
					list.add(num);
				} catch (NumberFormatException ex) {
					System.out.println(arr[i] + " 並非整數");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			if (list.size() == arr.length) // 確保輸入的都為整數，直到輸入正確為止
				break;
		}

		System.out.println("Origin unsorted list");
		for (int i = 0; i < list.size(); i++)
			System.out.print(list.get(i) + " ");
		System.out.println("");

		mergeSort(list, 0, list.size() - 1);// (資料,最左邊索引值,最右邊索引值)

		System.out.println("MergeSort by recursive method");
		for (int i = 0; i < list.size(); i++)
			System.out.print(list.get(i) + " ");

		scn.close();
	}

	public static void mergeSort(List<Integer> list, int left, int right) {
		if (left < right) { // 當左邊大於右邊時代表只剩一個元素了
			int mid = (left + right) / 2; // 每次對切，切到只剩一個為止
			mergeSort(list, left, mid); // 左邊等份
			mergeSort(list, mid + 1, right); // 右邊等份
			Merge(list, left, mid + 1, right); // 排序且合併
		}
	}

	public static void Merge(List<Integer> list, int left, int mid, int right) {
		int[] temp = new int[right + 1]; // 建立一個temp陣列存放排序後的值
		int left_end = mid - 1; // 左邊最後一個位置
		int index = left; // 位移起始點
		int origin_left = left; // 將最左邊的變數儲存起來(最後搬移元素會用到)

		while ((left <= left_end) && (mid <= right)) { // 左右兩串列比大小依序放入temp陣列中儲存
			if (list.get(left) <= list.get(mid))
				temp[index++] = list.get(left++);
			else
				temp[index++] = list.get(mid++);
		}

		if (left <= left_end) { // 若左邊的串列尚未走完將剩餘的數值依序放入temp陣列中
			while (left <= left_end) {
				temp[index++] = list.get(left++);
			}
		} else { // 反之若右邊的串列尚未走完將剩餘的數值依序放入temp陣列中
			while (mid <= right) {
				temp[index++] = list.get(mid++);
			}
		}
		// 最後將排序好的temp陣列複製到list串列中
		for (int i = origin_left; i <= right; i++) {
			list.set(i, temp[i]);
		}

	}

}
