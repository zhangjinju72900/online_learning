package com.tedu.common.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

public class VideoUtil {

	/**
	 * 获取指定视频的帧并保存为图片至指定目录
	 * 
	 * @param videofile
	 *            源视频文件路径
	 * @param framefile
	 *            截取帧的图片存放路径
	 * @throws Exception
	 */
	public static String fetchFrame(String videofile, String framefile) throws Exception {
		
		if(StringUtils.isBlank(framefile)){
			framefile = videofile.substring(0, videofile.lastIndexOf("."))+".jpg";
		}
		
		FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videofile); 
        ff.start();
        int lenght = ff.getLengthInFrames();

        File targetFile = new File(framefile);
        int i = 0;
        Frame frame = null;
        while (i < lenght) {
            // 过滤前5帧，避免出现全黑的图片，依自己情况而定
            frame = ff.grabFrame();
            if ((i > 5) && (frame.image != null)) {
                break;
            }
            i++;
        }      

        String imgSuffix = "jpg";
        if(framefile.indexOf('.') != -1){
            String[] arr = framefile.split("\\.");
            if(arr.length>=2){
                imgSuffix = arr[1];
            }
        }

        Java2DFrameConverter converter =new Java2DFrameConverter();
        BufferedImage srcBi =converter.getBufferedImage(frame);
        int owidth = srcBi.getWidth();
        int oheight = srcBi.getHeight();
        // 对截取的帧进行等比例缩放
        int width = 800;
        int height = (int) (((double) width / owidth) * oheight);
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        bi.getGraphics().drawImage(srcBi.getScaledInstance(width, height, Image.SCALE_SMOOTH),0, 0, null);
        try {
            ImageIO.write(bi, imgSuffix, targetFile);
        }catch (Exception e) {
            e.printStackTrace();
        }      
        ff.stop();
		return framefile;
	}

	public static boolean processImg(String veido_path, String ffmpeg_path) {
		File file = new File(veido_path);
		if (!file.exists()) {
			System.err.println("路径[" + veido_path + "]对应的视频文件不存在!");
			return false;
		}
		List<String> commands = new java.util.ArrayList<String>();
		commands.add(ffmpeg_path);
		commands.add("-i");
		commands.add(veido_path);
		commands.add("-y");
		commands.add("-f");
		commands.add("image2");
		commands.add("-ss");
		commands.add("5");// 这个参数是设置截取视频多少秒时的画面
		// commands.add("-t");
		// commands.add("0.001");
		commands.add("-s");
		commands.add("200x200");
		commands.add(veido_path.substring(0, veido_path.lastIndexOf(".")).replaceFirst("vedio", "file") + ".jpg");
		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commands);
			builder.start();
			System.out.println("截取成功");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void main(String[] args) {
		try {
			Date d = new Date();
			// fetchFrame("D:\\document\\work\\201807\\平台测试文件2\\汽车发动机管理系统检查与修理-实操1电控系统自诊断.mp4",
			// "");

			fetchFrame(
					"http://know-how001.oss-cn-beijing.aliyuncs.com/b1ee5fa0dfd44e9a97b15124a55c7b06201812100642292.mp4",
					"D:\\111444.jpg");
			
//			processImg("http://know-how001.oss-cn-beijing.aliyuncs.com/f49d0ddb7b32433c9aa16dba7284dc8f201812060741352.mp4", "D:\\1234.jpg");
			System.out.println(new Date().getTime() - d.getTime());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
