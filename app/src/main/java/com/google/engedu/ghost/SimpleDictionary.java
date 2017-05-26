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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;
    public static final int MIN_WORD_LENGTH = 4;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        Log.d("ghost", "getAnyWordStartingWith: word "+prefix);
        if(prefix.isEmpty()){
            Random random = new Random();
            int n = random.nextInt()%60000;
            Log.d("ghost", "getAnyWordStartingWith: is empty");
            return words.get(n);
        }
        else {
            int N = words.size();
            int l = 0, r = N - 1, mid;
            while (l <= r) {
                mid = (l + r)/2;
                String word = words.get(mid);
                if (word.startsWith(prefix)) {
                    return words.get(mid);
                } else if (prefix.compareTo(word) > 0) {
                    l = mid + 1;
                } else
                    r = mid - 1;
            }
            return null;
        }
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        String selected = null;
        return selected;
    }
}
