-- 智能体工具表
CREATE TABLE agent_tool (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(100) NOT NULL COMMENT '工具名称',
                            description TEXT COMMENT '工具描述',
                            category VARCHAR(50) COMMENT '工具分类',
                            system_prompt TEXT COMMENT '系统提示词',
                            model_name VARCHAR(100) DEFAULT 'gpt-4o' COMMENT '默认模型',
                            is_active BOOLEAN DEFAULT TRUE COMMENT '是否激活',
                            icon_url VARCHAR(255) COMMENT '图标URL',
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            INDEX idx_category (category),
                            INDEX idx_active (is_active)
) COMMENT='智能体工具表';

-- 对话会话表
CREATE TABLE agent_conversation (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    user_id VARCHAR(100) NOT NULL COMMENT '用户ID',
                                    tool_id BIGINT NOT NULL COMMENT '工具ID',
                                    title VARCHAR(255) DEFAULT '新对话' COMMENT '对话标题',
                                    model_name VARCHAR(100) DEFAULT 'gpt-4o' COMMENT '使用的模型',
                                    is_pinned BOOLEAN DEFAULT FALSE COMMENT '是否置顶',
                                    is_archived BOOLEAN DEFAULT FALSE COMMENT '是否归档',
                                    total_tokens INTEGER DEFAULT 0 COMMENT '总token消耗',
                                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                    FOREIGN KEY (tool_id) REFERENCES agent_tool(id) ON DELETE CASCADE,
                                    INDEX idx_user_tool (user_id, tool_id),
                                    INDEX idx_updated_at (updated_at)
) COMMENT='智能体对话会话表';

-- 消息记录表
CREATE TABLE agent_message (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               conversation_id BIGINT NOT NULL COMMENT '对话ID',
                               role ENUM('user', 'assistant', 'system') NOT NULL COMMENT '消息角色',
                               content LONGTEXT NOT NULL COMMENT '消息内容',
                               tokens_used INTEGER DEFAULT 0 COMMENT '使用的token数',
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               FOREIGN KEY (conversation_id) REFERENCES agent_conversation(id) ON DELETE CASCADE,
                               INDEX idx_conversation_time (conversation_id, created_at)
) COMMENT='智能体消息记录表';

-- 工具标签表
CREATE TABLE agent_tool_tag (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                tool_id BIGINT NOT NULL COMMENT '工具ID',
                                tag_name VARCHAR(50) NOT NULL COMMENT '标签名称',
                                FOREIGN KEY (tool_id) REFERENCES agent_tool(id) ON DELETE CASCADE,
                                UNIQUE KEY unique_tool_tag (tool_id, tag_name)
) COMMENT='智能体工具标签表';

-- 插入初始工具数据
INSERT INTO agent_tool (name, description, category, system_prompt, model_name, icon_url) VALUES
                                                                                              ('研究生助手', '专门为研究生提供学术指导和研究建议的智能助手', 'education', '你是一个专业的研究生学术助手，能够帮助研究生解答学术问题、提供研究建议、协助论文写作等。请用专业、友好的语气回答问题。', 'gpt-4o', '/static/icons/student-assistant.png'),
                                                                                              ('导师顾问', '为导师提供学生管理和学术指导建议的智能顾问', 'education', '你是一个经验丰富的学术导师顾问，能够帮助导师更好地指导学生、管理研究项目、处理学术事务。请提供专业、实用的建议。', 'gpt-4o', '/static/icons/supervisor-advisor.png'),
                                                                                              ('数据分析师', '协助进行数据分析、可视化和统计解释的智能助手', 'analytics', '你是一个专业的数据分析专家，擅长数据处理、统计分析、可视化展示和结果解释。请提供准确、详细的分析建议。', 'gpt-4o', '/static/icons/data-analyst.png'),
                                                                                              ('通用助手', '提供各种日常问题解答和任务协助的通用智能助手', 'general', '你是一个友好、乐于助人的AI助手，能够回答各种问题并提供有用的建议。请保持礼貌、准确和有用。', 'gpt-4o', '/static/icons/general-assistant.png');