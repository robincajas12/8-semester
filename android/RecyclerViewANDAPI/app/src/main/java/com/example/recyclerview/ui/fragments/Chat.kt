package com.example.recyclerview.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recyclerview.databinding.FragmentChatBinding
import com.example.recyclerview.ui.adapters.ChatAdapter
import com.example.recyclerview.ui.adapters.ChatMessage
import com.lottiefiles.dotlottie.core.model.Config
import com.lottiefiles.dotlottie.core.util.DotLottieSource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class Chat : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val adapter = ChatAdapter()
    private var typingJob: Job? = null
    private var nextId = 0L

    private val responses = listOf(
        "Hola 👋, ¿en qué puedo ayudarte?",
        "Interesante, cuéntame más...",
        "Entendido, estoy de acuerdo 🙂",
        "¿Podrías repetir eso, por favor?",
        "Estoy escribiendo una respuesta para ti..."
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.chatRecyclerView.layoutManager =
            LinearLayoutManager(requireContext()).apply { stackFromEnd = true }
        binding.chatRecyclerView.adapter = adapter

        submitAndScroll(listOf(ChatMessage(nextId++, "¡Hola! 👋", true)))

        binding.sendButton.setOnClickListener { sendMessage() }
    }

    private fun sendMessage() {
        val text = binding.messageInput.text?.toString()?.trim().orEmpty()
        if (text.isEmpty()) return
        binding.messageInput.text?.clear()

        typingJob?.cancel()
        typingJob = viewLifecycleOwner.lifecycleScope.launch {
            try {
                val current = adapter.currentList.toMutableList()
                current.add(ChatMessage(nextId++, text, false))
                submitAndScroll(current)

                val response = responses[Random.nextInt(responses.size)]
                // Cuanto más largo el mensaje, más tiempo "escribiendo"
                val typingTime = 500L + response.length * 70L
                var elapsed = 0L
                var animationLoaded = false

                while (elapsed < typingTime) {
                    val showTime =
                        Random.nextLong(600, 1201).coerceAtMost(typingTime - elapsed)
                    binding.typingIndicator.visibility = View.VISIBLE
                    if (!animationLoaded) {
                        binding.typingAnimation.post { loadTypingAnimation() }
                        animationLoaded = true
                    }
                    delay(showTime)
                    elapsed += showTime

                    if (elapsed < typingTime) {
                        // pausa: INVISIBLE mantiene el espacio, la UI no se mueve
                        val pauseTime =
                            Random.nextLong(300, 901).coerceAtMost(typingTime - elapsed)
                        binding.typingIndicator.visibility = View.INVISIBLE
                        delay(pauseTime)
                        elapsed += pauseTime
                    }
                }

                submitAndScroll(current + ChatMessage(nextId++, response, true))
            } finally {
                _binding?.typingIndicator?.visibility = View.GONE
            }
        }
    }

    private fun submitAndScroll(messages: List<ChatMessage>) {
        adapter.submitList(messages) { scrollToBottom() }
    }

    private fun loadTypingAnimation() {
        _binding?.typingAnimation?.load(
            Config.Builder()
                .source(DotLottieSource.Asset("texting.json"))
                .autoplay(true)
                .loop(true)
                .speed(1f)
                .build()
        )
    }

    private fun scrollToBottom() {
        binding.chatRecyclerView.post {
            if (_binding != null && adapter.itemCount > 0) {
                binding.chatRecyclerView.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    override fun onDestroyView() {
        typingJob?.cancel()
        _binding = null
        super.onDestroyView()
    }
}
