
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 爬取指定链接的一组MP3 文件
 *
 * 放入指定的目录中
 *
 * @author XuDong
 *
 */
public class Application {
    public static void main(String[] args) {

        System.out.println("请输入需要的单词发音类型：0-美式  1-英式");
        Scanner scanner = new Scanner(System.in);
        String targetType = scanner.next();
        if(targetType.equals("1") && targetType.equals("0")){
            System.out.println("您输入的是"+ targetType +"，请输入0或1，不支持其他类型！");
            return;
        }

        //有道api  美式：type=0   英式：type=0
        String baseUrl = "http://dict.youdao.com/dictvoice?type=" + targetType + "&audio=";

        System.out.println("请输入单词txt地址，如： E:\\YouDaoDownload\\ankiword.txt");
        scanner = new Scanner(System.in);
        String targetWordTxt = scanner.next();
        File file = new File(targetWordTxt);
        if(!file.exists()){
            System.out.println(targetWordTxt + "\t 文件目录不存在，请重新确认！");
            return;
        }
        if(!file.getName().endsWith(".txt")){
            System.out.println(targetWordTxt + "\t 不是txt结尾，请重新确认");
            return;
        }

        //获取单词列表
        BufferedReader reader = null;
        String lineWord = null;
        int line =1;

        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            while ((lineWord = reader.readLine()) != null) {
                System.out.print("Line"+ line);

                String word = "";

                //正则表达式  匹配每行的第一个单词
                String patt = "^\\b\\w*\\b";
                Pattern pattern = Pattern.compile(patt);
                Matcher matcher = pattern.matcher(lineWord);
                while (matcher.find()){
                    word = matcher.group();//返回匹配的字符串
                }

                //匹配每行的单词
                if(word.equals("")){
                    System.out.print("\t \t \t单词匹配失败");
                    continue;
                }
                System.out.print("\t" + word);

                String wordUrl = baseUrl+ word;
                DownloadUtils downloadUtils  = new DownloadUtils(wordUrl, word, "mp3",file.getParent()+"\\words");
                try {
                    downloadUtils.httpDownload();
                    System.out.print("\t \t \t下载成功");
                } catch (Exception e) {
                    System.out.print("\t \t \t下载失败");
                    e.printStackTrace();
                }
                System.out.println();

                line ++ ;
            }

            System.out.println("所有单词下载完成！总计：" + line--);
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
