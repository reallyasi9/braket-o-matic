import 'package:polymer/polymer.dart';
import 'package:exportable/exportable.dart';

@export
class Team extends JsProxy with Exportable{

	@export
	int id;

	@export
	int seed;

	@export
	double elo;

	@export
	String schoolName;

	@export
	String schoolShortName;

	@export
	String nickname;

	@export
	List<String> colors;

	@export
	String imageName;
}
