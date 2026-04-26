import json
import ollama

from flask import Flask, request

from utils import *


app = Flask(__name__)

@app.route("/chat", methods=["POST"])
def chat():
    query = request.json["query"]
    
    is_stream = request.args.get("stream", False)
    
    response = ollama.chat(
        model="deepseek-r1:1.5b",
        messages=[
            {"role": "user", "content": query}
        ],
        stream=is_stream,
    )
    
    if is_stream:
        def generate():
            for chunk in response:
                yield json.dumps({"answer": chunk["message"]["content"]}, ensure_ascii=False) + "\n"
        return generate()
    
    return {"answer": response["message"]["content"]}

@app.route("/think-and-answer", methods=["POST"])
def think_and_answer():
    query = request.json["query"]
    
    is_stream = request.args.get("stream", False)
    
    response = ollama.chat(
        model="deepseek-r1:1.5b",
        messages=[
            {"role": "user", "content": query}
        ],
        stream=is_stream,
    )
    if not is_stream:
        return separate_think_and_answer(response["message"]["content"])
    
    def generate():
        current_answer = ""
        for chunk in response:
            chunk_text = chunk["message"]["content"]
            current_answer += chunk_text
            if "</think>" not in current_answer:
                yield json.dumps({"think": remove_think_and_answer_tags(chunk_text), "answer": ""}, ensure_ascii=False) + "\n"
            else:
                yield json.dumps({"think": "", "answer": remove_think_and_answer_tags(chunk_text)}, ensure_ascii=False) + "\n"
    return generate()

if __name__ == "__main__":
    app.run(host="localhost", port=8001, debug=True)