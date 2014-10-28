PList Parser
====

## Usage

See code below.

```java
InputStream inputStream = ClassLoader.getSystemResourceAsStream("sample.plist");

PListDict dict = PListParser.parse(inputStream);

String code = dict.getString("code");
double val = dict.getReal("score");
Date date = dict.getDate("date");

PListDict others = dict.getDict("others");
PListArray array = dict.getArray("users");

```

And here is the sample.plist file (in assets folder).

```
<?xml version="1.0" encoding="UTF-8"?>
<plist version="1.0">
    <dict>
        <key>code</key>
        <string>54235582305924389532</string>
        <key>score</key>
        <real>43.42</real>
        <key>date</key>
        <date>2011-11-12T03:14:00Z</date>
        <key>others</key>
        <dict>
            <key>test</key>
            <true />
        </dict>
        <key>users</key>
        <array>
		    <dict>
		        <key>exist</key>
		        <true />
		        <key>id</key>
		        <integer>1</integer>
		        <key>name</key>
		        <string>test1</string>
		    </dict>
		    <dict>
		        <key>id</key>
		        <integer>2</integer>
		        <key>name</key>
		        <string>test2</string>
		        <key>exist</key>
		        <false />
		    </dict>
        </array>
    </dict>
</plist>
```

## License
```
Copyright 2011-2014 Keiji ARIYAMA

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```

## Author

[Keiji ARIYAMA](https://www.c-lis.co.jp)
