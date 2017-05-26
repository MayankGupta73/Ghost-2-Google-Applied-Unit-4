/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import android.util.Log;

import java.util.HashMap;
import java.util.Random;


public class TrieNode {
    private HashMap<String, TrieNode> children;
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String s) {
        TrieNode temp, curr = this;
        for(char ch: s.toCharArray()) {
            if (curr.children.containsKey(String.valueOf(ch))) {
                curr = curr.children.get(String.valueOf(ch));
            } else {
                temp = new TrieNode();
                curr.children.put(String.valueOf(ch), temp);
                curr = temp;
            }
        }

        curr.isWord = true;
    }

    public boolean isWord(String s) {
        TrieNode curr = this;
        for(char ch: s.toCharArray()){
            if(!curr.children.containsKey(String.valueOf(ch)))
                return false;
            curr = curr.children.get(String.valueOf(ch));
        }
        if(curr.isWord)
            return true;
        else
            return false;
    }

    public String getAnyWordStartingWith(String s) {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        TrieNode curr = this;
        for(char ch: s.toCharArray()){
            if(!curr.children.containsKey(String.valueOf(ch)))
                return null;
            curr = curr.children.get(String.valueOf(ch));
        }
        stringBuilder.append(s);
        while(!curr.isWord){
           if(curr.children.isEmpty() && !curr.isWord)
               return null;
            int length =  curr.children.keySet().toArray().length;
            int rand = Math.abs(random.nextInt()%(length));
            Log.d("ghost", "getAnyWordStartingWith: rand "+rand+ " length "+ length);
            String randomKey = (String) curr.children.keySet().toArray()[rand];
            stringBuilder.append(randomKey);
            curr = curr.children.get(randomKey);
        }

        return  stringBuilder.toString();
//        if(curr.isWord)
//            return stringBuilder.toString();
//        else
//            return null;
    }

    public String getGoodWordStartingWith(String s) {
        return null;
    }
}
