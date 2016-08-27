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

	Team.fromMap(Map<String, dynamic> obj) {
		this.id = obj["ID"];
		this.seed = obj["Seed"];
		this.elo = obj["Elo"];
		this.schoolName = obj["SchoolName"];
		this.schoolShortName = obj["SchoolShortName"];
		this.nickname = obj["Nickname"];
		this.colors = obj["Colors"];
		this.imageName = obj["ImageName"];
	}
}
