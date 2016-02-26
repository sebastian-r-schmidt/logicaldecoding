grammar PgLogicalDecoding;

logline
:
	(
		tx_statement
		| dml_statement
	) EOF
;

tx_statement
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

dml_statement
:
	'table ' table ': '
	(
		insert_op
		| update_op
		| delete_op
	)
;

insert_op
:
	'INSERT: '
	(
		new_key_value_pair
	)*
;

update_op
:
	'UPDATE: old-key: '
	(
		old_key_value_pair
	)* 'new-tuple: '
	(
		new_key_value_pair
	)*
;

delete_op
:
	'DELETE: '
	(
		old_key_value_pair
	)*
;

old_key_value_pair
:
	columnname '[' typedef ']:'
	(
		quoted_value
		| value
	)
	(
		' '
		| EOF
	)
;

new_key_value_pair
:
	columnname '[' typedef ']:'
	(
		quoted_value
		| value
	)
	(
		' '
		| EOF
	)
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

value
:
	~(' ')
;

quoted_value
:
	String
;

Identifier
:
	(
		Char
		| Num
		| Underscore
	)+
;

String
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