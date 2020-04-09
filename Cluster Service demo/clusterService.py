from flask import Flask, request, jsonify
from numpy import round
from pickle import load
import sklearn

model = load(open("working_knn_classifier.pkl", "rb"))

app = Flask(__name__)



@app.route("/", methods=['POST'])
def getDispersion():
	stat = request.get_json()
	data_prep = []
	data_prep.append(stat["Electrical"])
	data_prep.append(stat["Sport"])
	data_prep.append(stat["Vehicle and Parts"])
	data_prep.append(stat["Beautycare"])
	data_prep.append(stat["Cultural"])
	data_prep.append(stat["Home"])
	data_prep.append(stat["Gathering"])

	[pred] = round(model.predict_proba([data_prep]),2)
	return jsonify({"Electrical":pred[0] * 10, "Vehicle and Parts":pred[1] * 10, "Home":pred[2] * 10,
            "Cultural":pred[3] * 10, "Gathering":pred[4] * 10,
           "Beautycare":pred[5] * 10, "Sport":pred[6] * 10})

if __name__ == '__main__':
	app.run(debug=False,host='0.0.0.0')