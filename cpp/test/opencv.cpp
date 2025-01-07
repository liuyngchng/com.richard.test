/**
 * g++ opencv.cpp -o opencv `pkg-config --cflags --libs opencv4`
 */

#include <opencv2/highgui.hpp>
#include <opencv2/opencv.hpp>
using namespace cv;
using namespace std;
int main()
{
    string image_path = "/home/rd/Pictures/1.png";
    Mat img = imread(image_path, IMREAD_COLOR);

    imshow("Display window", img);
    int k = waitKey(0);              // Wait for a keystroke in the window
    return 0;
}
