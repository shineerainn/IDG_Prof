import re

def separate_think_and_answer(message):
    think = re.findall(r"\<think\>((?:.|\s)*?)\<\/think\>", message)
    answer = re.findall(r"(?<=<\/think\>)((?:.|\s)*?)$", message)

    if len(think) == 0 or len(answer) == 0:
        return {"think": "", "answer": message}
    else:
        return {"think": think[0], "answer": answer[0]}


def remove_think_and_answer_tags(text: str):
    for pattern in ["<think>", "</think>", "<answer>", "</answer>"]:
        text = text.replace(pattern, "")

    return text