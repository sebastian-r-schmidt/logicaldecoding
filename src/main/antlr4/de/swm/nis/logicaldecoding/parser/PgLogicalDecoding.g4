/*
Copyright (c) 2016 Sebastian Schmidt

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
grammar PgLogicalDecoding;

logline
:
	(
		txStatement
		| dmlStatement
	) EOF
;

txStatement
:
		('BEGIN ' Number )
		|( 'COMMIT ' Number (' (at 'commitTimestamp ')')?)
;

Number
:
	[0-9]+
;

commitTimestamp:
	Date ' ' Time
;

Date: 
	[0-9][0-9][0-9][0-9]'-'[0-9][0-9]'-'[0-9][0-9]
;

Time:
	[0-9][0-9]':'[0-9][0-9]':'[0-9][0-9]('+'|'-')([0-9])+
;
dmlStatement
:
	'table ' table ': '
	(
		insertOp
		| updateOp
		| deleteOp
	)
;

insertOp
:
	'INSERT: '
	(
		newKeyValuePair
	)*
;

updateOp
:
	'UPDATE: old-key: '
	(
		oldKeyValuePair
	)* 'new-tuple: '
	(
		newKeyValuePair
	)*
;

deleteOp
:
	'DELETE: '
	(
		oldKeyValuePair
	)*
;

oldKeyValuePair
:
	columnname '[' typedef ']:'
	(
		quotedValue
		| value
	)
	(
		' '
		| EOF
	)
;

newKeyValuePair
:
	columnname '[' typedef ']:'
	(
		quotedValue
		| value
	)
	(
		' '
		| EOF
	)
;

value
:
	~(' ')*
;

table
:
	schemaname '.' tablename
;

schemaname
:
	Identifier
;

tablename
:
	Identifier
;

columnname
:
	Identifier
;

typedef
:
	 ~(']:')*
;

quotedValue
:
	QuotedString
;

Identifier
:
	(
		Char
		| Num
		| Underscore
	)+
;

QuotedString
:
	'\'' ~( '\'' )* '\''
;

fragment
Num
:
	'-'?[0-9]
;

fragment
Char
:
	[a-zA-Z]
;

fragment
Underscore
:
	'_'
;