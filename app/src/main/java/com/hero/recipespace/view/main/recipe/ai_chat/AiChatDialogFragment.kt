package com.hero.recipespace.view.main.recipe.ai_chat

import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import com.hero.recipespace.BuildConfig
import com.hero.recipespace.R
import com.hero.recipespace.databinding.FragmentAiChatDialogBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AiChatDialogFragment : DialogFragment() {
    private var _binding: FragmentAiChatDialogBinding? = null
    private val binding get() = _binding!!

    private var prompt = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding =
            FragmentAiChatDialogBinding.inflate(
                inflater,
                container,
                false,
            )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setEditText()
        binding.tvGemini.movementMethod = ScrollingMovementMethod()

//        geminiTextOnly()
//        geminiImageView()
//        geminiChat()
//        streamChatOnlyText()
//        limitOption()
        harmfulSetOption()
    }

    // edit text 설정
    private fun setEditText() {
        binding.editMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                prompt = binding.editMessage.text.toString()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    // text 전용
    private fun geminiTextOnly() {
        val generativeModel = GenerativeModel(
            // Use a model that's applicable for your use case (see "Implement basic use cases" below)
            modelName = "gemini-pro",
            // Access your API key as a Build Configuration variable (see "Set up your API key" above)
            apiKey = BuildConfig.geminiKey
        )

        val chatHistory = mutableListOf<List<String>>()

        binding.mcvSend.setOnClickListener {

            chatHistory.add(listOf("당신", prompt))
            binding.rvChat.adapter = AiChatMessageAdapter(requireContext(), chatHistory)

            CoroutineScope(IO).launch {
                val response = generativeModel.generateContent(prompt)
                val responseText = response.text.toString()
                chatHistory.add(listOf("Gemini", responseText))

                withContext(Main) {
                    binding.rvChat.adapter = AiChatMessageAdapter(requireContext(), chatHistory)
                }
            }
            binding.editMessage.text?.clear()
        }
    }

    // image 분석
    private fun geminiImageView() {
        val generativeModel = GenerativeModel(
            // Use a model that's applicable for your use case (see "Implement basic use cases" below)
            modelName = "gemini-pro-vision",
            // Access your API key as a Build Configuration variable (see "Set up your API key" above)
            apiKey = BuildConfig.geminiKey
        )

        val image1 = BitmapFactory.decodeResource(this.resources, R.drawable.flower)
        val image2 = BitmapFactory.decodeResource(this.resources, R.drawable.flower2)

        val chatHistory = mutableListOf<List<String>>()

        binding.mcvSend.setOnClickListener {
            val inputContent = content {
                image(image1)
                image(image2)
                text(prompt)
            }

            chatHistory.add(listOf("당신", prompt))
            binding.rvChat.adapter = AiChatMessageAdapter(requireContext(), chatHistory)

            CoroutineScope(IO).launch {
                val response = generativeModel.generateContent(inputContent)
                val responseText = response.text.toString()
                chatHistory.add(listOf("Gemini", responseText))

                withContext(Main) {
                    binding.rvChat.adapter = AiChatMessageAdapter(requireContext(), chatHistory)
                }
            }

            binding.editMessage.text?.clear()
        }
    }

    private fun geminiChat() {
        val generativeModel = GenerativeModel(
            // Use a model that's applicable for your use case (see "Implement basic use cases" below)
            modelName = "gemini-pro",
            // Access your API key as a Build Configuration variable (see "Set up your API key" above)
            apiKey = BuildConfig.geminiKey
        )

        val chat = generativeModel.startChat(
            history = listOf(
                content(role = "user") { text("지금부터 너는 부동산 중개인이라고 생각하고 대화해줘") },
                content(role = "model") { text("알겠습니다.") },
            )
        )

        val chatHistory = mutableListOf<List<String>>()

        binding.mcvSend.setOnClickListener {

            chatHistory.add(listOf("당신", prompt))
            binding.rvChat.adapter = AiChatMessageAdapter(requireContext(), chatHistory)

            CoroutineScope(IO).launch {
                val response = chat.sendMessage(prompt)
                val aiResponse = response.text ?: "no return"
                chatHistory.add(listOf("Gemini", aiResponse))

                withContext(Main) {
                    binding.rvChat.adapter = AiChatMessageAdapter(requireContext(), chatHistory)
                }
            }

            binding.editMessage.text?.clear()
        }
    }

    // 스트리밍 채팅(이미지 포함) 사용
    private fun streamChatImage() {
        val generativeModel = GenerativeModel(
            // For text-and-image input (multimodal), use the gemini-pro-vision model
            modelName = "gemini-pro-vision",
            // Access your API key as a Build Configuration variable (see "Set up your API key" above)
            apiKey = BuildConfig.geminiKey
        )

        val image1 = BitmapFactory.decodeResource(this.resources, R.drawable.flower)
        val image2 = BitmapFactory.decodeResource(this.resources, R.drawable.flower)

        val inputContent = content {
            image(image1)
            image(image2)
            text(prompt)
        }

        binding.mcvSend.setOnClickListener {
            CoroutineScope(IO).launch {
                var fullResponse = ""

                generativeModel.generateContentStream(inputContent).collect { chunk ->
                    withContext(Main) {
                        binding.tvGemini.text = chunk.text
                    }

                    fullResponse += chunk.text
                }
            }
            binding.editMessage.text?.clear()
        }
    }

    // 스트리밍 채팅 온리 텍스트
    private fun streamChatOnlyText() {
        val generativeModel = GenerativeModel(
            // For text-and-image input (multimodal), use the gemini-pro-vision model
            modelName = "gemini-pro",
            // Access your API key as a Build Configuration variable (see "Set up your API key" above)
            apiKey = BuildConfig.geminiKey
        )

        val inputContentText = content { prompt }

        binding.mcvSend.setOnClickListener {
            CoroutineScope(IO).launch {
                var fullResponse = ""

                generativeModel.generateContentStream(inputContentText).collect { chunk ->
                    withContext(Main) {
                        binding.tvGemini.text = chunk.text
                    }

                    fullResponse += chunk.text
                }
            }

            binding.editMessage.text?.clear()
        }
    }

    // 콘텐츠 생성 제어
    private fun limitOption() {
        val config = generationConfig {
            temperature = 0.45f
            topK = 16
            topP = 0.1f
            maxOutputTokens = 200
            stopSequences = listOf("red")
        }

        val generativeModel = GenerativeModel(
            // 모델은 알맞게 변경
            modelName = "gemini-pro-vision",
            apiKey = BuildConfig.geminiKey,
            generationConfig = config,
            safetySettings = listOf(
                SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.NONE)
            )
        )

        val image1 = BitmapFactory.decodeResource(this.resources, R.drawable.leonardo_dicaprio_2010)

        val chatHistory = mutableListOf<List<String>>()

        binding.mcvSend.setOnClickListener {
            val inputContent = content {
                image(image1)
                text(prompt)
            }

            chatHistory.add(listOf("당신", prompt))
            binding.rvChat.adapter = AiChatMessageAdapter(requireContext(), chatHistory)

            CoroutineScope(IO).launch {
                val response = generativeModel.generateContent(inputContent)
                val responseText = response.text.toString()
                chatHistory.add(listOf("Gemini", responseText))

                withContext(Main) {
                    binding.rvChat.adapter = AiChatMessageAdapter(requireContext(), chatHistory)
                }
            }

            binding.editMessage.text?.clear()
        }
    }

    // 위험물 판단
    private fun harmfulSetOption() {
        val config = generationConfig {
            temperature = 1f
            topK = 16
            topP = 0.1f
            stopSequences = listOf("red")
        }

        val generativeModel = GenerativeModel(
            // 모델은 알맞게 변경
            modelName = "gemini-pro",
            apiKey = BuildConfig.geminiKey,
            generationConfig = config,
            safetySettings = listOf(
                SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.NONE),
                SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.NONE),
                SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.NONE),
                SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.NONE),
            )
        )

        val chatHistory = mutableListOf<List<String>>()

        binding.mcvSend.setOnClickListener {
            chatHistory.add(listOf("당신", prompt))
            binding.rvChat.adapter = AiChatMessageAdapter(requireContext(), chatHistory)

            CoroutineScope(IO).launch {
                try{
                    val response = generativeModel.generateContent(prompt)
                    val responseText = response.text.toString()
                    chatHistory.add(listOf("Gemini", responseText))

                    withContext(Main) {
                        binding.rvChat.adapter = AiChatMessageAdapter(requireContext(), chatHistory)
                    }
                } catch (e: Exception) {
                    Log.e("GeminiTest", e.message.toString())
                }
            }

            binding.editMessage.text?.clear()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        fun newInstance() = AiChatDialogFragment()

        const val TAG = "AiChatbot"
    }
}
