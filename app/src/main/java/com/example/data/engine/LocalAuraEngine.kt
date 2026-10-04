package com.example.data.engine

import com.example.data.model.AiTone

data class ProverbItem(
    val language: String,
    val originalText: String,
    val englishTranslation: String,
    val lifeMeaning: String
)

data class SlangItem(
    val word: String,
    val pronunciation: String,
    val meaning: String,
    val exampleSentence: String,
    val tag: String
)

data class RecipeItem(
    val name: String,
    val region: String,
    val prepTime: String,
    val description: String,
    val ingredients: List<String>,
    val steps: List<String>,
    val auraTip: String
)

object LocalAuraEngine {

    val dailyProverbs = listOf(
        ProverbItem(
            language = "isiZulu",
            originalText = "Umuntu ngumuntu ngabantu",
            englishTranslation = "A person is a person through other persons (Ubuntu)",
            lifeMeaning = "Our humanity is connected. When we support one another in our communities, we strengthen each other."
        ),
        ProverbItem(
            language = "Sesotho",
            originalText = "Kopano ke matla, karohano ke bofokoli",
            englishTranslation = "Unity is strength, division is weakness",
            lifeMeaning = "Whatever challenges you face, remember that community solidarity and teamwork get us through."
        ),
        ProverbItem(
            language = "isiXhosa",
            originalText = "Inkomo ihlinzwa ngokubambisana",
            englishTranslation = "A big task is accomplished by working together",
            lifeMeaning = "Heavy workloads become manageable when you share the effort and communicate openly."
        ),
        ProverbItem(
            language = "Afrikaans",
            originalText = "Aanhouer wen",
            englishTranslation = "The one who perseveres, wins",
            lifeMeaning = "Keep moving forward consistently, even when the road feels steep. Persistence pays off."
        ),
        ProverbItem(
            language = "Sepedi",
            originalText = "Mabogo dinku a thebana",
            englishTranslation = "Hands wash each other",
            lifeMeaning = "Mutual support and cooperation make families and neighborhoods thrive."
        ),
        ProverbItem(
            language = "Setswana",
            originalText = "Lore lo ojwa lo sale metsi",
            englishTranslation = "A stick is shaped while it is still flexible",
            lifeMeaning = "Build solid foundations and positive daily habits early on."
        )
    )

    val slangDictionary = listOf(
        SlangItem(
            word = "Howzit",
            pronunciation = "how-zit",
            meaning = "A standard informal greeting; short for 'how is it going?'",
            exampleSentence = "Howzit bra! How's your week looking?",
            tag = "Greeting"
        ),
        SlangItem(
            word = "Lekker",
            pronunciation = "leh-kuhr",
            meaning = "Great, good, enjoyable, delicious, or pleasant.",
            exampleSentence = "That food was really lekker, thanks!",
            tag = "Expression"
        ),
        SlangItem(
            word = "Eish",
            pronunciation = "aysh",
            meaning = "An expression of shock, surprise, sympathy, or disbelief.",
            exampleSentence = "Eish, the traffic on the N1 this morning was terrible.",
            tag = "Reaction"
        ),
        SlangItem(
            word = "Yebo",
            pronunciation = "yeh-boh",
            meaning = "isiZulu for 'Yes' or 'Agreed / Indeed'.",
            exampleSentence = "Yebo, we are definitely ready for the weekend.",
            tag = "Agreement"
        ),
        SlangItem(
            word = "Sharp sharp / Sho",
            pronunciation = "shahp-shahp / show",
            meaning = "All good, goodbye, thank you, or confirmed.",
            exampleSentence = "I'll see you at the meeting tomorrow, sharp sharp!",
            tag = "Sign-off"
        ),
        SlangItem(
            word = "Now-now vs Just now",
            pronunciation = "now-now / just now",
            meaning = "'Now-now' means immediately or in a few minutes. 'Just now' means later or sometime in the indefinite future.",
            exampleSentence = "I'm leaving now-now (5 mins) vs I will look at that document just now (later today).",
            tag = "Concept"
        ),
        SlangItem(
            word = "Braai",
            pronunciation = "br-eye",
            meaning = "South African barbecue: grilling meat over wood or charcoal coals with friends and family.",
            exampleSentence = "We're lighting the fire for a braai this Saturday afternoon.",
            tag = "Culture"
        ),
        SlangItem(
            word = "Robot",
            pronunciation = "roh-bot",
            meaning = "Traffic light in South African English.",
            exampleSentence = "Turn right at the second robot after the shopping centre.",
            tag = "Everyday"
        ),
        SlangItem(
            word = "Grootman",
            pronunciation = "groot-mahn",
            meaning = "An older, respected mentor, community figure, or elder brother.",
            exampleSentence = "The grootman gave some sound financial advice.",
            tag = "Respect"
        ),
        SlangItem(
            word = "Mzansi",
            pronunciation = "m-zahn-see",
            meaning = "Affectionate nickname for South Africa, from the Xhosa word 'uMzantsi'.",
            exampleSentence = "There's no place with energy quite like Mzansi.",
            tag = "Identity"
        )
    )

    val localRecipes = listOf(
        RecipeItem(
            name = "Durban Bunny Chow",
            region = "KwaZulu-Natal / Durban",
            prepTime = "45 mins",
            description = "A hollowed-out fresh loaf of white bread filled with rich mutton, beef, or bean curry, served with grated carrot sambals.",
            ingredients = listOf(
                "1 loaf unsliced white bread",
                "500g mutton, beef, or sugar beans",
                "2 chopped onions, 3 garlic cloves, 1 tbsp minced ginger",
                "2 tbsp Durban masala or curry powder",
                "2 chopped tomatoes and fresh curry leaves",
                "Grated carrot and vinegar salad (sambals) to serve"
            ),
            steps = listOf(
                "Sauté onions until golden, then add garlic, ginger, curry leaves, and masala.",
                "Add meat or soaked beans and tomatoes, simmering gently until tender and the gravy is thick.",
                "Cut bread into halves or quarters and hollow out the center dough.",
                "Fill the bread cavity with hot curry and top with the scooped bread to dip."
            ),
            auraTip = "Eat with your hands using the scooped-out bread breadcrumb 'virgin' to soak up the gravy."
        ),
        RecipeItem(
            name = "Chakalaka & Pap",
            region = "Across South Africa",
            prepTime = "35 mins",
            description = "Spicy vegetable braai relish made with carrots, peppers, and baked beans, served with traditional maize meal pap.",
            ingredients = listOf(
                "2 cups coarse maize meal + 2 cups boiling water",
                "2 grated carrots, 1 chopped green pepper, 1 chopped onion",
                "1 tin baked beans, 2 chopped tomatoes",
                "1 tbsp curry powder or Rajah spice, 2 garlic cloves"
            ),
            steps = listOf(
                "For pap: Pour maize meal into salted boiling water, stir to desired consistency (stiff or crumbly phutu), cover and steam on low for 25 mins.",
                "For chakalaka: Fry onions, peppers, and garlic in oil.",
                "Add curry powder and grated carrots, cooking for 5 minutes.",
                "Stir in baked beans and simmer until well combined and thick."
            ),
            auraTip = "Great served warm or chilled alongside grilled chops and boerewors."
        ),
        RecipeItem(
            name = "Cape Malay Bobotie",
            region = "Western Cape",
            prepTime = "50 mins",
            description = "Spiced minced beef baked with bay leaves, raisins, and a savory egg-custard topping, traditionally served with yellow turmeric rice.",
            ingredients = listOf(
                "500g lean minced beef",
                "1 slice white bread soaked in milk",
                "1 diced onion, 2 tsp turmeric, 1 tbsp curry powder",
                "2 tbsp fruit chutney (Mrs. Ball's style)",
                "2 tbsp seedless raisins and 3 bay leaves",
                "2 eggs beaten with 1/2 cup milk"
            ),
            steps = listOf(
                "Sauté onion with spices, minced beef, squeezed bread, raisins, and chutney. Simmer for 10 mins.",
                "Transfer to a baking dish and press flat. Top with bay leaves.",
                "Pour the egg and milk mixture over the top.",
                "Bake at 180°C for 30 minutes until the custard layer is golden and set."
            ),
            auraTip = "Serve with yellow rice with raisins and sweet chutney on the side."
        )
    )

    fun generateLocalResponse(
        prompt: String,
        persona: String = "MZANSI",
        tone: AiTone = AiTone.MZANSI_CASUAL
    ): String {
        val lower = prompt.lowercase().trim()
        val isFormal = (tone == AiTone.FORMAL_ENGLISH)

        // Greeting
        if (lower.contains("hello") || lower.contains("hi") || lower.contains("howzit") || lower.contains("dumela") || lower.contains("sawubona") || lower == "hey") {
            return if (isFormal) {
                "Good day. I am Aura, your South African AI companion. I am pleased to assist you today. How may I be of service?"
            } else {
                "Howzit! Sawubona! I'm Aura. You can chat with me in English, Zulu, Sepedi, Afrikaans, or mix it up with Mzansi slang. What's on your mind today?"
            }
        }

        // Loadshedding
        if (lower.contains("load shedding") || lower.contains("loadshedding") || lower.contains("eskom") || lower.contains("power") || lower.contains("stage") || lower.contains("schedule")) {
            val matchedArea = LoadsheddingLookupEngine.findAreaByQuery(lower)
            if (matchedArea != null) {
                val status = LoadsheddingLookupEngine.calculateAreaStatus(matchedArea, 2)
                val slots = matchedArea.scheduleByStage[2]?.joinToString("\n") { "• ${it.day}: ${it.startTime} - ${it.endTime}" } ?: "No outages"
                return if (isFormal) {
                    "Load Shedding Timetable for ${matchedArea.suburb} (${matchedArea.municipality}, Block ${matchedArea.blockNumber}):\n\n" +
                            "${status.formattedStatus}\n\n" +
                            "Stage 2 Scheduled Time Windows:\n$slots\n\n" +
                            "Advisory: You can examine other municipal blocks and stages within the Loadshedding menu."
                } else {
                    "Loadshedding Schedule for ${matchedArea.suburb} (${matchedArea.municipality} Block ${matchedArea.blockNumber}):\n\n" +
                            "${status.formattedStatus}\n\n" +
                            "Stage 2 Scheduled Slots:\n$slots\n\n" +
                            "Tip: Check the Loadshedding tab on the top bar to search other suburbs or switch stages anytime!"
                }
            }

            return if (isFormal) {
                "Load shedding represents a persistent infrastructural challenge in South Africa. Please consider the following precautions:\n\n" +
                        "1. Ensure auxiliary power supplies (power banks and portable batteries) are charged during operational hours.\n" +
                        "2. Boil water and retain it in an insulated thermal flask ahead of scheduled outages.\n" +
                        "3. Disconnect sensitive computing and home electronics to prevent voltage surge damage upon grid restoration.\n" +
                        "4. Inspect rechargeable emergency LED illumination in primary living spaces.\n\n" +
                        "Consult the Loadshedding section for area-specific timetables and block definitions."
            } else {
                "Eish, loadshedding is always a headache. Here are practical ways to stay prepared:\n\n" +
                        "1. Keep your power bank and essential devices charged whenever the power is on.\n" +
                        "2. Boil a kettle and fill a thermos flask before the scheduled outage so you have hot water ready.\n" +
                        "3. Unplug sensitive electronics (TVs, PCs, fridges) to protect against grid surges when power returns.\n" +
                        "4. Have rechargeable LED lights or battery lanterns stationed in key rooms.\n\n" +
                        "Check the Loadshedding tab on top for your local suburb's exact schedule and block times."
            }
        }

        // Tough day / stress / work
        if (lower.contains("sad") || lower.contains("tough") || lower.contains("stress") || lower.contains("tired") || lower.contains("rough") || lower.contains("bad day")) {
            return if (isFormal) {
                "I am deeply sorry to hear that you are having a difficult day. Balancing professional obligations with daily responsibilities in South Africa can often be demanding.\n\n" +
                        "I encourage you to take time to decompress this evening—whether through quiet reflection, rest, or discussing the situation. What took place today?"
            } else {
                "Eish, sorry to hear you're going through a rough patch. Life in SA can be demanding with work, daily hustle, and everyday pressures.\n\n" +
                        "Take a moment to unwind this evening — whether that's a cup of tea, stepping away from screens, or chatting through what's bothering you. What happened today?"
            }
        }

        // Local languages
        if (lower.contains("zulu") || lower.contains("isizulu") || lower.contains("xhosa") || lower.contains("sepedi") || lower.contains("sotho") || lower.contains("afrikaans") || lower.contains("translate")) {
            return if (isFormal) {
                "Certainly. Here is a curated selection of essential greetings across South Africa's official languages:\n\n" +
                        "• isiZulu: 'Sawubona' (Hello), 'Unjani?' (How are you?), 'Ngiyaphila' (I am well), 'Ngiyabonga' (Thank you).\n" +
                        "• Sepedi: 'Dumela' (Hello), 'O kae?' (How are you?), 'Ke gona' (I am fine), 'Ke a leboga' (Thank you).\n" +
                        "• isiXhosa: 'Molo' (Hello), 'Kunjani?' (How are you?), 'Ndiphilile' (I am well), 'Enkosi' (Thank you).\n" +
                        "• Afrikaans: 'Goeiedag' (Good day), 'Hoe gaan dit?' (How are you?), 'Baie dankie' (Thank you very much).\n\n" +
                        "Please inform me if you would like grammatical guidance or translation of specific statements."
            } else {
                "Sure! Here are some common everyday phrases across South African languages:\n\n" +
                        "• isiZulu: 'Sawubona' (Hello), 'Unjani?' (How are you?), 'Ngiyaphila' (I'm well), 'Ngiyabonga' (Thank you).\n" +
                        "• Sepedi: 'Dumela' (Hello), 'O kae?' (How are you?), 'Ke gona' (I am fine), 'Ke a leboga' (Thank you).\n" +
                        "• isiXhosa: 'Molo' (Hello), 'Kunjani?' (How are things?), 'Ndiphilile' (I'm well), 'Enkosi' (Thank you).\n" +
                        "• Afrikaans: 'Goeiedag' (Good day), 'Hoe gaan dit?' (How's it going?), 'Baie dankie' (Thank you very much).\n\n" +
                        "Let me know if you want to translate a specific sentence or practice a phrase."
            }
        }

        // Food & Braai
        if (lower.contains("recipe") || lower.contains("food") || lower.contains("dinner") || lower.contains("braai") || lower.contains("cook") || lower.contains("bunny chow")) {
            return if (isFormal) {
                "South African gastronomy features iconic, time-honored traditional specialties:\n\n" +
                        "1. Durban Bunny Chow: A hollowed loaf of fresh white bread filled with spiced mutton, beef, or sugar bean curry.\n" +
                        "2. Traditional Pap and Chakalaka: Wholesome maize meal porridge accompanied by spiced vegetable relish.\n" +
                        "3. Cape Malay Bobotie: Gently spiced minced beef baked under a golden egg-custard topping.\n\n" +
                        "Comprehensive recipes and cooking methodologies may be reviewed in the Recipes section."
            } else {
                "For a solid local meal, you can't go wrong with:\n\n" +
                        "1. Durban Bunny Chow (hollowed loaf filled with aromatic mutton or bean curry).\n" +
                        "2. Phutu Pap with spicy homemade Chakalaka and boerewors.\n" +
                        "3. Cape Malay Bobotie with yellow turmeric rice and Mrs. Ball's chutney.\n\n" +
                        "You can tap the Food icon on the top bar to read the full ingredients and step-by-step recipes!"
            }
        }

        // Slang & phrases
        if (lower.contains("slang") || lower.contains("meaning") || lower.contains("word") || lower.contains("just now") || lower.contains("now now")) {
            return if (isFormal) {
                "In South African English colloquial usage, 'now-now' and 'just now' represent nuances in temporal intention:\n\n" +
                        "• 'Now-now' indicates an immediate or near-term action (within moments).\n" +
                        "• 'Just now' indicates a prospective action deferred to later in the day or an indefinite time.\n\n" +
                        "You may inspect the Explore section to browse definitions and cultural origins of South African expressions."
            } else {
                "'Now-now' and 'Just now' are classic Mzansi timing:\n\n" +
                        "• 'Now-now' = Right away, in a couple of minutes.\n" +
                        "• 'Just now' = Sometime later today (or whenever they get around to it).\n\n" +
                        "Check the Explore tab on top to browse the complete Mzansi slang phrasebook!"
            }
        }

        // General normal response
        return if (isFormal) {
            when (persona) {
                "UBUNTU" -> "I acknowledge your thoughts. Grounded solidarity and reciprocal human empathy remain cornerstone values for navigating our shared experiences. In what manner would you prefer to address this matter?"
                "KASI" -> "An insightful observation. In our South African context, perseverance and innovative problem-solving lead to tangible progress. Please provide additional details so we may structure our next steps."
                else -> "Understood. As your South African AI companion, I am prepared to assist you with factual guidance, regional context, translations, or general discourse. How may we proceed?"
            }
        } else {
            when (persona) {
                "UBUNTU" -> "I hear you. Staying grounded and supporting the people around us is what keeps things moving forward. How would you like to handle this?"
                "KASI" -> "Sharp points. In Mzansi, there's always a practical way to solve a problem. Tell me more about what you're working on and let's figure out the next step."
                else -> "Got it. As your South African companion, I'm here to help with whatever you need — whether that's advice, local info, translation, or daily conversation. What's next?"
            }
        }
    }
}
