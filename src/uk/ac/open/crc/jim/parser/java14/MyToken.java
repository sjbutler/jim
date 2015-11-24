/*
 Copyright (C) 2010-2015 The Open University

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package uk.ac.open.crc.jim.parser.java14;

public class MyToken extends Token
{
  /**
   * Constructs a new token for the specified Image and Kind.
   * @param kind determined by JavaCC
   * @param image lexical content matching token
   */
  public MyToken(int kind, String image)
  {
     this.kind = kind;
     this.image = image;
  }

  int realKind = Java14ParserConstants.GT;

  /**
   * Returns a new Token object.
   * @param ofKind determined by JavaCC
   * @param tokenImage lexical content matching token
   * @return a {@code Token) of the specified kind
   */
  public static final Token newToken(int ofKind, String tokenImage)
  {
    return new MyToken(ofKind, tokenImage);
  }
}
