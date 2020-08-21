package com.company;

// 535_Encode_and_Decode_TinyURL
// https://leetcode.com/problems/encode-and-decode-tinyurl/
// https://www.lintcode.com/problem/tiny-url/

import java.util.HashMap;
import java.util.Random;

public class Problem535_Encode_and_Decode_TinyURL {
    final static int ENCODE_LEN = 6;
    final static String BASE = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    final static int BASE_LEN = BASE.length(); // should be 62; // 如果是16进制，则BASE_LEN是16；// 如果是10进制，则是10；
    final static String prefix = "www.reemy.com/";

    // Option #1 62-Base Number System
    static int incremental_id = 650000;
    static HashMap<Integer, String> map_id_lstr = new HashMap<>();

    // 【Chosen Solution for Option #1】
    // 2nd trial: 直接使用String(而不是StringBuffer)来进行操作；
    // 8/21/2020: Warning from git Code Analysis: String Concatenation '+' in loop;
    // 如果在loop中+的话，compiler无法将其自动变为StringBuilder的方式；每次重新分配String内存的方式效率比较低；
    public static String encode_base62(String longUrl) {
        StringBuilder shortUrl = new StringBuilder();
        int id = incremental_id;
        map_id_lstr.put(incremental_id, longUrl);
        incremental_id++;
        while (id > 0) {
            int index = id % BASE_LEN;
            shortUrl.insert(0, BASE.charAt(index));
            // shortUrl = BASE.charAt(index) + shortUrl;
            id /= BASE_LEN;
        }
        while (shortUrl.length() < 6) {
            shortUrl.insert(0, "0");
            // shortUrl = "0" + shortUrl;
        }
        shortUrl.insert(0, prefix);
        // shortUrl = prefix + shortUrl;
        return new String(shortUrl);
    }

    // 7/23/20: 考虑十六进制字符串 F0 是如何转换成为10进数字 240 的? 然后再套入下面的for loop公式；
    public static String decode_base62(String shortUrl) {
        int id  = 0;
        shortUrl = shortUrl.substring(prefix.length());
        // 这个公式可以用于任何的 N进制字符串转换为10 进制数字;
        for (int i = 0; i < shortUrl.length(); i++) {
            int index = BASE.indexOf(shortUrl.charAt(i));
            id =  id * BASE_LEN + index;
        }
        return map_id_lstr.get(id);
    }

    // Option #2 Random chars
    static HashMap<String, String> map_l_s = new HashMap<>();
    static HashMap<String, String> map_s_l = new HashMap<>();

    public static String encode_2(String longUrl) {
        if (map_l_s.containsKey(longUrl)) {
            return map_l_s.get(longUrl);
        }
        StringBuilder sb = new StringBuilder();
        boolean done = false;
        Random rand = new Random();
        // String shortUrl = new String(); // git >> code analysis >> Warning: new String() is redundant;
        while (!done) {
            for (int i = 0; i < ENCODE_LEN; i++) {
                sb.append(BASE.charAt(rand.nextInt(BASE_LEN)));
            }
            if (!map_s_l.containsKey(new String(sb))) {
                done = true;
            }
        }
        String shortUrl = new String(sb);
        shortUrl = prefix + shortUrl;
        map_l_s.put(longUrl, shortUrl);
        map_s_l.put(shortUrl, longUrl);

        return shortUrl;
    }

    public static String decode_2(String shortUrl) {
        return map_s_l.get(shortUrl);
    }

    public static void main(String[] args) {
        String longURL = "www.google.com";
        String shortURL = encode_base62(longURL);
        System.out.printf("[62-base-Encode]longURL:%s\nshortURL:%s\n", longURL, shortURL);
        longURL = decode_base62(shortURL);
        System.out.printf("[62-base-Decode]shortURL:%s\nlongURL:%s\n", shortURL, longURL);

        shortURL = encode_2(longURL);
        System.out.printf("[randomized-Encode]longURL:%s\nshortURL:%s\n", longURL, shortURL);
        longURL = decode_2(shortURL);
        System.out.printf("[randomized-Decode]shortURL:%s\nlongURL:%s\n", shortURL, longURL);

        System.out.printf("decode(encode(%s)):%s\n", longURL, decode_base62(encode_base62(longURL)));

        longURL = "https://leetcode.com/problems/design-tinyurl";
        shortURL = encode_base62(longURL);
        System.out.printf("[62-base-Encode]longURL:%s\nshortURL:%s\n", longURL, shortURL);
        longURL = decode_base62(shortURL);
        System.out.printf("[62-base-Decode]shortURL:%s\nlongURL:%s\n", shortURL, longURL);

        shortURL = encode_2(longURL);
        System.out.printf("[randomized-Encode]longURL:%s\nshortURL:%s\n", longURL, shortURL);
        longURL = decode_2(shortURL);
        System.out.printf("[randomized-Decode]shortURL:%s\nlongURL:%s\n", shortURL, longURL);

        System.out.printf("decode(encode(%s)):%s\n", longURL, decode_base62(encode_base62(longURL)));

    }

    // 1st trial: 用了StringBuffer来操作，每次insert在最前面，比较复杂；
    public static String encode_base62_used_sb(String longUrl) {
        StringBuilder sb = new StringBuilder();
        int id = incremental_id;
        map_id_lstr.put(incremental_id, longUrl);
        incremental_id++;
        do {
            int index = id % BASE_LEN;
            // sb.append(BASE.charAt(index));
            sb.insert(0, BASE.charAt(index));
            id /= BASE_LEN;
        } while (id != 0);
        int paddingLen = ENCODE_LEN - sb.length();
        for (int i = 0; i < paddingLen; i++) {
            sb.insert(0, 0);
        }
        sb.insert(0, prefix);

        return new String(sb);
    }

}
