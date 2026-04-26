import json
import time
import unittest
import requests as rq

class ChatServiceTest(unittest.TestCase):
    def test_chat(self):
        response = rq.post(
            url="http://localhost:8081/chat",
            json={
                "query": "为什么天空是蓝色的?"
            },
        )

        answer = response.json()["answer"]
        
        self.assertIsInstance(answer, str)            
        self.assertRegex(answer, r"\<think\>(.|\s)*?\<\/think\>(.|\s)*")


    def test_chat_stream(self):
        response = rq.post(
            url="http://localhost:8081/chat?stream=true",
            json={
                "query": "为什么天空是蓝色的?"
            },
            stream=True
        )
        
        complete_answer = ""
        for line in response.iter_lines():
            line = json.loads(line)
            self.assertIsInstance(line, dict)
            self.assertIn("answer", line)
            complete_answer += line["answer"]
            print(line["answer"], end="")
            time.sleep(0.1)


        self.assertRegex(complete_answer, r"\<think\>(.|\s)*?\<\/think\>(.|\s)*")


    def test_think_and_answer(self):
        response = rq.post(
            url="http://localhost:8081/think-and-answer",
            json={
                "query": "为什么天空是蓝色的?"
            }
        )

        think = response.json()["think"]
        answer = response.json()["answer"]
                
        self.assertIsInstance(think, str)
        self.assertIsInstance(answer, str)
        for pattern in ["<think>", "</think>", "<answer>", "</answer>"]:
            self.assertNotIn(pattern, think)
            self.assertNotIn(pattern, answer)


    def test_think_and_answer_stream(self):
        response = rq.post(
            url="http://localhost:8081/think-and-answer?stream=true",
            json={
                "query": "为什么天空是蓝色的?"
            },
            stream=True
        )
        
        complete_think = ""
        complete_answer = ""
        for line in response.iter_lines():
            line = json.loads(line)
            self.assertIsInstance(line, dict)
            self.assertIn("think", line)
            self.assertIn("answer", line)
            complete_think += line["think"]
            complete_answer += line["answer"]
            print(line["think"], end="")
            print(line["answer"], end="")
            time.sleep(0.1)
            
        for pattern in ["<think>", "</think>", "<answer>", "</answer>"]:
            self.assertNotIn(pattern, complete_think)
            self.assertNotIn(pattern, complete_answer)


if __name__ == "__main__":
    unittest.main()