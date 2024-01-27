# UNDER DEVELOPMENT


# Assistant 
Compose Chat app using Kotlin. Very simple UI includes preview every component to easy to understand Compose not require additional fake data or other method.  
Advantages:
- Fast and Easy
- Multi language
- Simple but elegant UI.

## About

## Key Features
This app uses the latest libraries and tools:
- 100% written in Kotlin;
- Compose;
- StateFlow;
- Proto datastore for user preference no java-lite.
- MVVM architecture;
- Clean Architecture;
- Google Speech-Text streaming (gRPC);
- OpenAi Gpt3.5 streaming;
- Koin Dependency Injection for KMM
- Text to Speech in Hungarian
- Advanced Preview where ViewModel used.
  
## Development require:
- add GPT4

## Conclusion:
THIS PROJECT SHOULD NOT BE USED IN PRODUCTION.
- The Google credential json is stored in "assests" folder for the simplicity. To use this in production would require an authentication server to store json credential.

Dependency Injection used.
- Demonstrates how to pass Context safely to Viewmodel.

This is an experimental project using Compose LazyColum. 
- Store conversation in LiveData

Conversations stored in ViewModel as MutableList<ConversationModel> .
However there is a problem with adding or changing item value in exiting conversationModel.
Adding to the liveData like this: https://developer.android.com/topic/libraries/architecture/livedata#update_livedata_objects
does not trigger the observer when recompensation happens but add a new ConversationModel object to a list of Conversations or the other solution to use HashMap.

- By Remember

Following this https://developer.android.com/jetpack/compose/performance/bestpractices#avoid-backwards
We can use “var count by remember { mutableStateOf(0) }” to store our list items would be nice if data does not come from streaming API otherwise does not work.

- AutoScrolling in LazyColumn when streaming.
  
Referring to this article https://developer.android.com/jetpack/compose/lists#react-to-scroll-position
this require addition code to add lazyColumn
"
val scrollState = rememberScrollState()
LaunchedEffect(favourites.size) {
    scrollState.animateScrollTo(scrollState.maxValue)
}
"

- Take away
  
There has been a lot of boiler plate code to make this working with LiveData and manual dependecy injection so in this usecase to use Flow and Dagger would be much better option.


## Video:



https://github.com/sollarp/voice-assistant-chatgpt/assets/74240451/72ab5dee-bb69-49d9-a246-f9b42fce8fc2




## License
```
Assistant
Copyright (C) 2022  Peter Szollar

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or 
any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
```

