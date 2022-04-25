import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfInt4;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class MaBibliothequeTraitementImage {
	//Contient toutes les méthodes necessaires à la transformation des images


	//Methode qui permet de transformer une matrice intialement au  format BGR au format HSV
	public static Mat transformeBGRversHSV(Mat matriceBGR){
		Mat matriceHSV = new Mat(matriceBGR.height(),matriceBGR.cols(),matriceBGR.type());
		Imgproc.cvtColor(matriceBGR,matriceHSV,Imgproc.COLOR_BGR2HSV);
		return matriceHSV;

	}

	//Methode qui convertit une matrice avec 3 cannaux en un vecteur de 3 matrices monocanal (un canal par couleur)
	public static Vector<Mat> splitHSVChannels(Mat input) {
		Vector<Mat> channels = new Vector<Mat>(); 
		Core.split(input, channels);
		return channels;
	}

	//Methode qui permet d'afficher une image sur un panel
	public static void afficheImage(String title, Mat img){
		MatOfByte matOfByte=new MatOfByte();
		Highgui.imencode(".png",img,matOfByte);
		byte[] byteArray=matOfByte.toArray();
		BufferedImage bufImage=null;
		try{
			InputStream in=new ByteArrayInputStream(byteArray);
			bufImage=ImageIO.read(in);
			JFrame frame=new JFrame();
			frame.setTitle(title);
			frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
			frame.pack();
			frame.setVisible(true);

		}
		catch(Exception e){
			e.printStackTrace();
		}


	}

	//Methode qui permet de saturer les couleurs rouges à partir de 3 seuils
	public static Mat seuillage(Mat input, int seuilRougeOrange, int seuilRougeViolet,int seuilSaturation){
		// Décomposition en 3 canaux HSV
		Vector<Mat> channels = splitHSVChannels(input);
		
		// Création d'un seuil
		Scalar RougeViolet = new Scalar(seuilRougeViolet);
		Scalar RougeOrange = new Scalar(seuilRougeOrange);
		Scalar Saturation = new Scalar(seuilSaturation);
		
		//Création d'une matrice
		Mat RougeV = new Mat();
		Mat RougeO = new Mat();
		Mat Sat = new Mat();
		Mat pixel_rouge = new Mat();
		Mat pixel_noir = new Mat();
		
		// Comparaison et saturation des pixels rouges
		Core.compare(channels.get(0), RougeViolet, RougeV, Core.CMP_GT);
		Core.compare(channels.get(0), RougeOrange, RougeO, Core.CMP_LT);
		Core.compare(channels.get(1), Saturation, Sat, Core.CMP_GT);
		
		Core.bitwise_or(RougeV, RougeO, pixel_rouge);
		Core.bitwise_and(pixel_rouge, Sat, pixel_noir);  
		
		return pixel_noir; //Image saturée à retourner
		}



}

