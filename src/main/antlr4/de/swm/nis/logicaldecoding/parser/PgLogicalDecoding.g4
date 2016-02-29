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
	(
		'BEGIN '
		| 'COMMIT '
	) Number
;

Number
:
	[0-9]+
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
	~(' ')
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

//old position of value

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
	[0-9]
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