
import SwiftUI

struct ContentView: View {
    @State private var score = 0
    @State private var position = CGPoint(x: 180, y: 350)

    var body: some View {
        GeometryReader { geometry in
            ZStack {
                Color.black.ignoresSafeArea()

                VStack {
                    Text("H")
                        .font(.system(size: 28, weight: .bold))
                        .foregroundColor(.white)

                    Text("\(score)")
                        .font(.system(size: 48, weight: .bold))
                        .foregroundColor(.white)

                    Spacer()
                }
                .padding(.top, 30)

                Text("H")
                    .font(.system(size: 100, weight: .black))
                    .foregroundColor(.white)
                    .position(position)
                    .onTapGesture {
                        score += 1

                        withAnimation(.spring()) {
                            position = CGPoint(
                                x: CGFloat.random(in: 70...geometry.size.width - 70),
                                y: CGFloat.random(in: 150...geometry.size.height - 100)
                            )
                        }
                    }
            }
            .onAppear {
                position = CGPoint(
                    x: geometry.size.width / 2,
                    y: geometry.size.height / 2
                )
            }
        }
        .preferredColorScheme(.dark)
    }
}

#Preview {
    ContentView()
}
