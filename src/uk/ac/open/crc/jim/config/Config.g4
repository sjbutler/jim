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
grammar Config;

file: 
        definition+
    ;

definition:
              name '=' value ';'
          ;

name:
        NAME
    ;

value: 
      string_value
     | numeric_value
     | boolean_value
     ;



string_value: ;

numeric_value: ;

boolean_value: 
             'true'
             |'false'
             ;

// does the quoted hyphen cause problems 
// in practice? If so, I will need to find another solution
NAME: [a-zA-Z][a-zA-Z0-9]*('-'[a-zA-Z0-9][a-zA-Z0-9])*;

TRUE: 'true';

FALSE: 'false';


EQUALS: '=' ;
SEMI_COLON: ';';

// standard rules
//
// Whitespace and comments
//

WS  :  [ \t\r\n]+ -> skip
    ;

LINE_COMMENT
    :   '//' ~[\r\n]* -> skip
    ;
